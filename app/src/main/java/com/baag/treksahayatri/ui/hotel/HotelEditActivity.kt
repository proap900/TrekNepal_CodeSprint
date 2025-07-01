package com.baag.treksahayatri.ui.hotel

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.baag.treksahayatri.R
import com.baag.treksahayatri.data.model.Hotel
import com.baag.treksahayatri.data.model.RoomCategory
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class HotelEditActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1001
    private var imageUri: Uri? = null
    private val storage = FirebaseStorage.getInstance()
    private val viewModel = HotelViewModel()

    private lateinit var etName: EditText
    private lateinit var etLocation: EditText
    private lateinit var ivPreview: ImageView
    private lateinit var etSingleTotal: EditText
    private lateinit var etSingleAvail: EditText
    private lateinit var etSingleBooked: EditText
    private lateinit var etDoubleTotal: EditText
    private lateinit var etDoubleAvail: EditText
    private lateinit var etDoubleBooked: EditText
    private lateinit var etSuiteTotal: EditText
    private lateinit var etSuiteAvail: EditText
    private lateinit var etSuiteBooked: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_edit)

        etName = findViewById(R.id.etHotelName)
        etLocation = findViewById(R.id.etHotelLocation)
        ivPreview = findViewById(R.id.ivHotelPreviewImage)
        etSingleTotal = findViewById(R.id.etSingleTotal)
        etSingleAvail = findViewById(R.id.etSingleAvail)
        etSingleBooked = findViewById(R.id.etSingleBooked)
        etDoubleTotal = findViewById(R.id.etDoubleTotal)
        etDoubleAvail = findViewById(R.id.etDoubleAvail)
        etDoubleBooked = findViewById(R.id.etDoubleBooked)
        etSuiteTotal = findViewById(R.id.etSuiteTotal)
        etSuiteAvail = findViewById(R.id.etSuiteAvail)
        etSuiteBooked = findViewById(R.id.etSuiteBooked)
        btnSave = findViewById(R.id.btnSave)

        ivPreview.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        viewModel.fetchHotel()
        viewModel.hotel.observe(this) { hotel ->
            etName.setText(hotel.name)
            etLocation.setText(hotel.location)
            Glide.with(this).load(hotel.imageUrl).into(ivPreview)

            hotel.roomCategories.forEach {
                when (it.type) {
                    "Single" -> {
                        etSingleTotal.setText(it.total.toString())
                        etSingleAvail.setText(it.available.toString())
                        etSingleBooked.setText(it.booked.toString())
                    }
                    "Double" -> {
                        etDoubleTotal.setText(it.total.toString())
                        etDoubleAvail.setText(it.available.toString())
                        etDoubleBooked.setText(it.booked.toString())
                    }
                    "Suite" -> {
                        etSuiteTotal.setText(it.total.toString())
                        etSuiteAvail.setText(it.available.toString())
                        etSuiteBooked.setText(it.booked.toString())
                    }
                }
            }
        }

        btnSave.setOnClickListener {
            if (imageUri != null) {
                val fileRef = storage.reference.child("hotel_images/${UUID.randomUUID()}.jpg")
                fileRef.putFile(imageUri!!).continueWithTask { fileRef.downloadUrl }
                    .addOnSuccessListener { uri ->
                        saveHotel(uri.toString())
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                    }
            } else {
                val current = viewModel.hotel.value?.imageUrl ?: ""
                saveHotel(current)
            }
        }
    }

    private fun saveHotel(imageUrl: String) {
        val hotel = Hotel(
            id = viewModel.hotel.value?.id ?: "",
            name = etName.text.toString(),
            location = etLocation.text.toString(),
            imageUrl = imageUrl,
            roomCategories = listOf(
                RoomCategory("Single", etSingleTotal.text.toString().toInt(), etSingleAvail.text.toString().toInt(), etSingleBooked.text.toString().toInt()),
                RoomCategory("Double", etDoubleTotal.text.toString().toInt(), etDoubleAvail.text.toString().toInt(), etDoubleBooked.text.toString().toInt()),
                RoomCategory("Suite", etSuiteTotal.text.toString().toInt(), etSuiteAvail.text.toString().toInt(), etSuiteBooked.text.toString().toInt())
            )
        )
        viewModel.saveHotel(hotel)
        Toast.makeText(this, "Hotel info saved", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data?.data != null) {
            imageUri = data.data
            Glide.with(this).load(imageUri).into(ivPreview)
        }
    }
}

