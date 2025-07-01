package com.baag.treksahayatri.ui.guide

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.baag.treksahayatri.R
import com.baag.treksahayatri.data.model.GuideMedia
import com.baag.treksahayatri.databinding.ActivityGuideMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class GuideMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGuideMainBinding
    private lateinit var navController: NavController

    //for fab
    private lateinit var mediaPickerLauncher: ActivityResultLauncher<String>
    private lateinit var storage: FirebaseStorage
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentUserId: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate view using ViewBinding
        binding = ActivityGuideMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle system window insets (padding)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        // Setup custom bottom navigation clicks
        binding.navChat.setOnClickListener {
            safeNavigate(R.id.nav_chat)
        }

        binding.navHistory.setOnClickListener {
            safeNavigate(R.id.nav_history)
        }

        binding.navNotifications.setOnClickListener {
            safeNavigate(R.id.nav_notifications)
        }

        binding.navProfile.setOnClickListener {
            safeNavigate(R.id.nav_profile)
        }

        //for fabs
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()

        mediaPickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val type = contentResolver.getType(it)
                val isImage = type?.startsWith("image") == true
                val isVideo = type?.startsWith("video") == true

                if (isImage || isVideo) {
                    val mediaType = if (isImage) "image" else "video"
                    uploadMedia(it, mediaType)
                }
            }
        }


        binding.fabAdd.setOnClickListener {
            mediaPickerLauncher.launch("*/*")
        }
    }
    private fun uploadMedia(uri: Uri, mediaType: String) {
        val path = "guide_media/$currentUserId/${mediaType}s/${UUID.randomUUID()}"
        val ref = storage.reference.child(path)

        ref.putFile(uri)
            .continueWithTask { task -> ref.downloadUrl }
            .addOnSuccessListener { downloadUri ->
                val media = GuideMedia(url = downloadUri.toString(), type = mediaType)
                firestore.collection("guide_media").document(currentUserId)
                    .collection("items")
                    .add(media)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun safeNavigate(destinationId: Int) {
        if (navController.currentDestination?.id != destinationId) {
            navController.navigate(destinationId)
        }
    }
}