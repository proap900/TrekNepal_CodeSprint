package com.baag.treksahayatri.ui.hotel

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.baag.treksahayatri.R


class HotelAnalyticsActivity : AppCompatActivity() {

    private val viewModel = HotelViewModel()

    private lateinit var singleCount: TextView
    private lateinit var doubleCount: TextView
    private lateinit var suiteCount: TextView

    private lateinit var availableBar: ProgressBar
    private lateinit var bookedBar: ProgressBar
    private lateinit var availableLabel: TextView
    private lateinit var bookedLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_analytics)

        singleCount = findViewById(R.id.tvSingleCount)
        doubleCount = findViewById(R.id.tvDoubleCount)
        suiteCount = findViewById(R.id.tvSuiteCount)

        availableBar = findViewById(R.id.pbAvailable)
        bookedBar = findViewById(R.id.pbBooked)
        availableLabel = findViewById(R.id.tvAvailablePercent)
        bookedLabel = findViewById(R.id.tvBookedPercent)

        viewModel.fetchHotel()
        viewModel.hotel.observe(this) { hotel ->
            val categories = hotel.roomCategories

            val single = categories.find { it.type == "Single" }
            val double = categories.find { it.type == "Double" }
            val suite = categories.find { it.type == "Suite" }

            singleCount.text = "Single: ${single?.total ?: 0}"
            doubleCount.text = "Double: ${double?.total ?: 0}"
            suiteCount.text = "Suite: ${suite?.total ?: 0}"

            val total = categories.sumOf { it.total }
            val booked = categories.sumOf { it.booked }
            val available = categories.sumOf { it.available }

            if (total > 0) {
                availableBar.max = total
                bookedBar.max = total
                availableBar.progress = available
                bookedBar.progress = booked

                availableLabel.text = "Available: ${(available * 100 / total)}%"
                bookedLabel.text = "Booked: ${(booked * 100 / total)}%"
            } else {
                availableBar.visibility = View.GONE
                bookedBar.visibility = View.GONE
                availableLabel.text = "No data"
                bookedLabel.text = ""
            }
        }
    }
}
