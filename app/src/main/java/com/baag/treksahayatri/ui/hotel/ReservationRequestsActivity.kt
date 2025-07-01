package com.baag.treksahayatri.ui.hotel

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baag.treksahayatri.R
import com.baag.treksahayatri.data.model.Reservation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class ReservationRequestsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReservationAdapter
    private val reservations = mutableListOf<Reservation>()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_requests)

        recyclerView = findViewById(R.id.rvReservations)
        adapter = ReservationAdapter(reservations,
            onAccept = { updateStatus(it.id, "accepted") },
            onReject = { updateStatus(it.id, "rejected") }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchReservations()
    }

    private fun fetchReservations() {
        val hotelId = auth.currentUser?.uid ?: return
        val reservationsRef = db.collection("hotels").document(hotelId).collection("reservations")

        reservationsRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty) {
                // Add a sample reservation (only if empty)
                val mockReservation = Reservation(
                    travellerId = "mockT1",
                    travellerName = "Demo Traveller",
                    date = "2025-07-01",
                    time = "14:00",
                    status = "pending"
                )

                reservationsRef.add(mockReservation).addOnSuccessListener {
                    listenToReservations(reservationsRef)
                }
            } else {
                listenToReservations(reservationsRef)
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to fetch reservations.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun listenToReservations(ref: CollectionReference) {
        ref.addSnapshotListener { snapshot, e ->
            if (e != null) return@addSnapshotListener

            reservations.clear()

            snapshot?.documents?.forEach {
                val reservation = it.toObject(Reservation::class.java)
                reservation?.let { res -> reservations.add(res.copy(id = it.id)) }
            }

            adapter.notifyDataSetChanged()
        }
    }


    private fun updateStatus(reservationId: String, newStatus: String) {
        val hotelId = auth.currentUser?.uid ?: return

        val hotelRef = db.collection("hotels").document(hotelId)
        val reservationRef = hotelRef.collection("reservations").document(reservationId)

        reservationRef.get().addOnSuccessListener { doc ->
            val reservation = doc.toObject(Reservation::class.java) ?: return@addOnSuccessListener

            // Get current room categories
            hotelRef.get().addOnSuccessListener { hotelDoc ->
                val roomCategories = hotelDoc["roomCategories"] as? List<Map<String, Any>> ?: return@addOnSuccessListener

                val category = reservation.roomCategory
                val index = roomCategories.indexOfFirst { it["type"] == category }

                if (index == -1) {
                    Toast.makeText(this, "Invalid room category!", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val categoryMap = roomCategories[index]
                val available = (categoryMap["available"] as? Long ?: 0L).toInt()
                val booked = (categoryMap["booked"] as? Long ?: 0L).toInt()
                val total = (categoryMap["total"] as? Long ?: 0L).toInt()

                if (newStatus == "accepted") {
                    if (available <= 0) {
                        // ❌ No room available → reject automatically
                        reservationRef.update("status", "rejected")
                        Toast.makeText(this, "No room available in $category. Automatically rejected.", Toast.LENGTH_SHORT).show()
                    } else {
                        // ✅ Accept reservation & update counts
                        val updatedCategory = mapOf(
                            "type" to category,
                            "total" to total,
                            "available" to available - 1,
                            "booked" to booked + 1
                        )

                        // Rebuild updated list
                        val updatedCategories = roomCategories.toMutableList()
                        updatedCategories[index] = updatedCategory

                        // Update room category list + reservation status
                        hotelRef.update("roomCategories", updatedCategories).addOnSuccessListener {
                            reservationRef.update("status", "accepted").addOnSuccessListener {
                                Toast.makeText(this, "Reservation accepted.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    // Reject
                    reservationRef.update("status", "rejected")
                        .addOnSuccessListener {
                            Toast.makeText(this, "Reservation rejected.", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

}
