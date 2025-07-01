package com.baag.treksahayatri.ui.guide.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baag.treksahayatri.data.model.GuideMedia
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class GuideGalleryViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _mediaItems = MutableLiveData<List<GuideMedia>>()
    val mediaItems: LiveData<List<GuideMedia>> = _mediaItems

    fun loadMedia(uid: String, filterType: String) {
        firestore.collection("guide_media").document(uid).collection("items")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val filtered = snapshot.documents.mapNotNull { it.toObject(GuideMedia::class.java) }
                        .filter { it.type == filterType }
                    _mediaItems.value = filtered
                }
            }
    }
}
