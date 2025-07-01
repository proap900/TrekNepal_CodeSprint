package com.baag.treksahayatri.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.baag.treksahayatri.MainActivity
import com.baag.treksahayatri.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.baag.treksahayatri.R
import com.baag.treksahayatri.ui.guide.GuideMainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient

    private val roles = listOf("traveller", "guide", "hotel")
    private val RC_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase Init
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Spinner Init
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        binding.spinnerRole.adapter = adapter

        // Google Sign-In setup
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // from google-services.json
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Click listeners
        binding.btnLogin.setOnClickListener { loginWithEmail() }
        binding.btnGoogle.setOnClickListener { signInWithGoogle() }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun loginWithEmail() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val selectedRole = binding.spinnerRole.selectedItem.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fields must not be empty", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: ""
                fetchRoleAndNavigate(uid, selectedRole)
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Login failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun fetchRoleAndNavigate(uid: String, selectedRole: String) {
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                binding.progressBar.visibility = View.GONE
                if (document.exists()) {
                    val roleFromDb = document.getString("role") ?: ""
                    if (roleFromDb.equals(selectedRole, ignoreCase = true)) {
                        navigateToRole(roleFromDb)
                    } else {
                        Toast.makeText(this, "Role mismatch. You selected $selectedRole but you are $roleFromDb.", Toast.LENGTH_LONG).show()
                        auth.signOut()
                    }
                } else {
                    Toast.makeText(this, "User role not found.", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                }
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                auth.signOut()
            }
    }

    private fun navigateToRole(role: String) {
        val intent = when (role.lowercase()) {
            "traveller" -> Intent(this, MainActivity::class.java)
            "guide" -> Intent(this, GuideMainActivity::class.java)
            "hotel" -> Intent(this, MainActivity::class.java)
            else ->null
        }
        intent?.let {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
            finish()
        }
    }

    // Google Sign-In Flow
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                checkUserInFirestoreOrCreate(it.user)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Google login failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun checkUserInFirestoreOrCreate(user: FirebaseUser?) {
        val uid = user?.uid ?: return
        val selectedRole = binding.spinnerRole.selectedItem.toString()

        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val role = document.getString("role") ?: selectedRole
                    if (role.equals(selectedRole, ignoreCase = true)) {
                        navigateToRole(role)
                    } else {
                        Toast.makeText(this, "Role mismatch. Your role is $role.", Toast.LENGTH_LONG).show()
                        auth.signOut()
                    }
                } else {
                    // First-time Google login — assign selected role
                    val newUser = hashMapOf(
                        "email" to user.email,
                        "name" to user.displayName,
                        "role" to selectedRole,
                        "loginMethod" to "google"
                    )
                    firestore.collection("users").document(uid).set(newUser)
                        .addOnSuccessListener {
                            navigateToRole(selectedRole)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to save user: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
    }
}