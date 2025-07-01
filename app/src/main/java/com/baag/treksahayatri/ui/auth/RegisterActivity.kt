package com.baag.treksahayatri.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.baag.treksahayatri.data.model.User
import com.baag.treksahayatri.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val roles = listOf("traveller", "guide", "hotel")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase Init
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Role Spinner
        val roleAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        binding.roleSpinner.adapter = roleAdapter

        // Password toggle visibility
        binding.passwordToggle.setOnCheckedChangeListener { _, isChecked ->
            binding.etPassword.inputType =
                if (isChecked) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        binding.confirmPasswordToggle.setOnCheckedChangeListener { _, isChecked ->
            binding.confirmPasswordInput.inputType =
                if (isChecked) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        // Register button click
        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        // Already have account?
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // (Optional) Disable Facebook button
        binding.btnFacebook.visibility = View.GONE
    }

    private fun registerUser() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.confirmPasswordInput.text.toString().trim()
        val selectedRole = binding.roleSpinner.selectedItem.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show()
            return
        }

        // Proceed to Firebase Auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener
                val userData = User(
                    uid = uid,
                    name = name,
                    email = email,
                    role = selectedRole,
                    loginMethod = "email"
                )

                // Save to Firestore
                firestore.collection("users").document(uid)
                    .set(userData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to save user: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Registration failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}