package com.baag.treksahayatri.ui.traveller.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.baag.treksahayatri.data.model.GuideProfile
import com.baag.treksahayatri.databinding.BottomSheetEditProfileTravellerBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.bumptech.glide.Glide
import java.util.*

class EditProfileBottomSheetTraveller : BottomSheetDialogFragment() {

	private lateinit var binding: BottomSheetEditProfileTravellerBinding
	private val firestore = FirebaseFirestore.getInstance()
	private val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
	private var selectedImageUri: Uri? = null

	companion object {
		private const val REQUEST_IMAGE_PICK = 1001
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = BottomSheetEditProfileTravellerBinding.inflate(inflater, container, false)

		loadCurrentData()

		binding.ivProfile.setOnClickListener {
			pickImage()
		}

		binding.btnSave.setOnClickListener {
			saveProfile()
		}

		return binding.root
	}

	private fun loadCurrentData() {
		firestore.collection("travellerprofile").document(uid).get()
			.addOnSuccessListener { doc ->
				val guide = doc.toObject(GuideProfile::class.java)
				guide?.let {
					binding.etName.setText(it.name)
					binding.etDescription.setText(it.description)
					binding.etPhone.setText(it.phoneNumber)
					binding.etAddress.setText(it.address)



					if (it.photoUrl.isNotBlank()) {
						Glide.with(this)
							.load(it.photoUrl)
							.centerCrop()
							.into(binding.ivProfile)
					}
				}
			}
			.addOnFailureListener {
				Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
			}
	}

	private fun pickImage() {
		val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
		startActivityForResult(intent, REQUEST_IMAGE_PICK)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
			selectedImageUri = data?.data
			binding.ivProfile.setImageURI(selectedImageUri)
		}
	}

	private fun saveProfile() {
		val name = binding.etName.text.toString().trim()
		val description = binding.etDescription.text.toString().trim()
		val phone = binding.etPhone.text.toString().trim()
		val address = binding.etAddress.text.toString().trim()
		val email = FirebaseAuth.getInstance().currentUser?.email ?: ""

		if (name.isBlank() || email.isBlank()) {
			Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
			return
		}

		binding.btnSave.isEnabled = false
		binding.btnSave.text = "Saving..."

		if (selectedImageUri != null) {
			uploadProfileImage { imageUrl ->
				updateUser(name, email, description, phone, address, imageUrl)
			}
		} else {
			updateUser(name, email, description, phone, address, null)
		}
	}

	private fun uploadProfileImage(onUploaded: (String) -> Unit) {
		val ref = FirebaseStorage.getInstance()
			.reference.child("profile_images_guide/$uid/${UUID.randomUUID()}.jpg")
		val uploadTask = ref.putFile(selectedImageUri!!)

		uploadTask.addOnSuccessListener {
			ref.downloadUrl.addOnSuccessListener { uri ->
				onUploaded(uri.toString())
			}
		}.addOnFailureListener {
			Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
			binding.btnSave.isEnabled = true
			binding.btnSave.text = "Save"
		}
	}

	private fun updateUser(
		name: String,
		email: String,
		description: String,
		phone: String,
		address: String,
		imageUrl: String?

	) {
		val data = hashMapOf<String, Any>(
			"uid" to uid,
			"name" to name,
			"email" to email,
			"description" to description,
			"phone" to phone,
			"address" to address
		)
		imageUrl?.let { data["photoUrl"] = it }

		firestore.collection("travellerprofile").document(uid).set(data)
			.addOnSuccessListener {
				Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
				dismiss()
			}
			.addOnFailureListener {
				Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
				binding.btnSave.isEnabled = true
				binding.btnSave.text = "Save"
			}
	}
}
