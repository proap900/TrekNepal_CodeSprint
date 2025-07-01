package com.baag.treksahayatri.ui.guide.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baag.treksahayatri.data.model.GuideMedia
import com.baag.treksahayatri.data.model.GuideProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

class ProfileViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _profile = MutableLiveData<GuideProfile>()
    val profile: LiveData<GuideProfile> get() = _profile

    private val _mediaItems = MutableLiveData<List<GuideMedia>>()
    val mediaItems: LiveData<List<GuideMedia>> get() = _mediaItems

    fun loadProfile(uid: String) {
        firestore.collection("guideprofile").document(uid).get()
            .addOnSuccessListener { doc ->
                doc?.toObject(GuideProfile::class.java)?.let {
                    _profile.postValue(it)
                }
            }
    }

    fun loadMedia(uid: String, type: String) {
        firestore.collection("guide_media").document(uid)
            .collection("items")
            .whereEqualTo("type", type)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, _ ->
                snap?.let {
                    val list = it.documents.mapNotNull { d -> d.toObject(GuideMedia::class.java) }
                    _mediaItems.postValue(list)
                }
            }
    }

    fun deleteMedia(uid: String, media: GuideMedia) {
        val path = "guide_media/$uid/${media.type}s/${media.url.substringAfterLast("/")}"
        val ref = storage.getReferenceFromUrl(media.url)

        ref.delete().addOnSuccessListener {
            firestore.collection("guide_media").document(uid)
                .collection("items")
                .whereEqualTo("url", media.url)
                .get()
                .addOnSuccessListener { snap ->
                    snap.documents.forEach { it.reference.delete() }
                }
        }
    }

    fun updateAvailability(uid: String, available: Boolean) {
        firestore.collection("guideprofile").document(uid)
            .update("isAvailable", available)
    }
}
