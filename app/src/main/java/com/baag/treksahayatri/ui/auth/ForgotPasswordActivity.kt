package com.baag.treksahayatri.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.baag.treksahayatri.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnResetPassword.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            showProgress(true) // Show progress bar

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    showProgress(false) // Hide progress bar
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                        finish() // Close activity
                    } else {
                        Toast.makeText(this, "Failed to send reset email", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun showProgress(isVisible: Boolean) {
        binding.progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.btnResetPassword.isEnabled = !isVisible
    }
}