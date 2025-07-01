package com.baag.treksahayatri.ui.hotel

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.baag.treksahayatri.R
import com.baag.treksahayatri.data.model.Hotel
import com.baag.treksahayatri.databinding.ActivityHotelMainBinding
import com.baag.treksahayatri.ui.auth.LoginActivity
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth


class HotelMainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var titleText: TextView
    private lateinit var locationText: TextView
    private lateinit var totalText: TextView
    private lateinit var availableText: TextView
    private lateinit var bookedText: TextView

    private lateinit var editBtn: MaterialButton
    private lateinit var analyticsBtn: MaterialButton
    private lateinit var logoutBtn: MaterialButton
    private lateinit var notifyFab: FloatingActionButton

    private val viewModel = HotelViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_main)

        imageView = findViewById(R.id.hotel_gallery_image)
        titleText = findViewById(R.id.dashboard_title)
        locationText = findViewById(R.id.hotel_location)
        totalText = findViewById(R.id.total_rooms_value)
        availableText = findViewById(R.id.available_rooms_value)
        bookedText = findViewById(R.id.booked_rooms_value)

        editBtn = findViewById(R.id.edit_info_button)
        analyticsBtn = findViewById(R.id.view_analytics_button)
        logoutBtn = findViewById(R.id.logout_button)
        notifyFab = findViewById(R.id.fab_order_notification)

        setupListeners()
        viewModel.observeHotel()

        viewModel.hotel.observe(this) { hotel ->
            updateUI(hotel)
        }
    }

    private fun setupListeners() {
        editBtn.setOnClickListener {
            startActivity(Intent(this, HotelEditActivity::class.java))
        }

        analyticsBtn.setOnClickListener {
            startActivity(Intent(this, HotelAnalyticsActivity::class.java))
        }

        logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        notifyFab.setOnClickListener {
           startActivity(Intent(this, ReservationRequestsActivity::class.java))
        }
    }

    private fun updateUI(hotel: Hotel) {
        titleText.text = "Welcome, ${hotel.name}"
        locationText.text = hotel.location

        val total = hotel.roomCategories.sumOf { it.total }
        val available = hotel.roomCategories.sumOf { it.available }
        val booked = hotel.roomCategories.sumOf { it.booked }

        totalText.text = total.toString()
        availableText.text = available.toString()
        bookedText.text = booked.toString()

        Glide.with(this)
            .load(hotel.imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(imageView)
    }

}
