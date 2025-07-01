package com.baag.treksahayatri.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.baag.treksahayatri.MainActivity
import com.baag.treksahayatri.R
import com.baag.treksahayatri.ui.guide.GuideMainActivity
import com.baag.treksahayatri.ui.hotel.HotelMainActivity
import com.baag.treksahayatri.ui.traveller.TravellerMainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SplashActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        firestore = FirebaseFirestore.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val uid = user.uid
                firestore.collection("users").document(uid).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val role = document.getString("role")?.lowercase()
                            when (role) {
                                "traveller" -> startActivity(Intent(this, TravellerMainActivity::class.java))
                                "guide" -> startActivity(Intent(this, GuideMainActivity::class.java)) // Replace with GuideMainActivity if available
                                "hotel" -> startActivity(Intent(this, HotelMainActivity::class.java)) // Replace with HotelMainActivity if available
                                else -> {
                                    Toast.makeText(this, "Unknown role: $role", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                }
                            }
                        } else {
                            Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 1500) // 1.5 seconds splash delay for better UX
    }
}