package com.baag.treksahayatri.ui.hotel


import com.baag.treksahayatri.data.model.Hotel
import com.baag.treksahayatri.data.model.RoomCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await



class HotelRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun getOrCreateHotel(): Hotel? {
        val uid = auth.currentUser?.uid ?: return null
        val docRef = db.collection("hotels").document(uid)
        val snapshot = docRef.get().await()

        return if (snapshot.exists()) {
            snapshot.toObject(Hotel::class.java)?.copy(id = uid)
        } else {
            val defaultHotel = Hotel(
                id = uid,
                name = "My Hotel",
                location = "Not set",
                imageUrl = "",
                roomCategories = listOf(
                    RoomCategory("Single", 0, 0, 0),
                    RoomCategory("Double", 0, 0, 0),
                    RoomCategory("Suite", 0, 0, 0)
                )
            )
            docRef.set(defaultHotel).await()
            defaultHotel
        }
    }
    fun observeHotelRealtime(
        onChange: (Hotel?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance().collection("hotels")
            .document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }
                val hotel = snapshot?.toObject(Hotel::class.java)?.copy(id = uid)
                onChange(hotel)
            }
    }

    suspend fun updateHotel(hotel: Hotel) {
        if (hotel.id.isNotBlank()) {
            db.collection("hotels").document(hotel.id).set(hotel).await()
        }
    }
}
