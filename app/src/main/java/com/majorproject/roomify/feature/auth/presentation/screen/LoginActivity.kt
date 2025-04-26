package com.majorproject.roomify.feature.auth.presentation.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.majorproject.roomify.MainActivity
import com.majorproject.roomify.R
import com.majorproject.roomify.feature.auth.presentation.screen.RegisterActivity
import com.majorproject.roomify.feature.auth.presentation.viewmodel.AuthViewModel
import com.majorproject.roomify.feature.common.presentation.models.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient
    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var createAccountText: TextView
    private lateinit var googleSignInButton: ImageView
    private lateinit var facebookSignInButton: ImageView
    private lateinit var forgotPasswordText: TextView

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    val credential = GoogleAuthProvider.getCredential(it.idToken, null)
                    viewModel.googleSignIn(credential)
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Make sure this matches your layout file name
        window.statusBarColor = getColor(android.R.color.white)
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setupViews()
        setupGoogleSignIn()
        observeAuthState()
        setupClickListeners()
    }

    private fun setupViews() {
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signInButton = findViewById(R.id.signInButton) // Add this ID to your button in XML
        createAccountText = findViewById(R.id.createAccountText) // Add this ID to your TextView in XML
        googleSignInButton = findViewById(R.id.googleSignInButton) // Add this ID to your ImageView in XML
        facebookSignInButton = findViewById(R.id.facebookSignInButton) // Add this ID to your ImageView in XML
        forgotPasswordText = findViewById(R.id.forgotPasswordText) // Add this ID to your TextView in XML
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("908201615697-ih7m7t1ffelitbg6psqjm8het7j5hdl1.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun observeAuthState() {
        lifecycleScope.launch {
            viewModel.authState.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        // Show loading state (optional)
                    }
                    is Resource.Success -> {
                        val user = state.data
                        if (user != null) {
                            navigateToMainScreen()
                        }
                    }
                    is Resource.Error -> {
                        if (state.message!!.contains("malformed", ignoreCase = true) ||
                            state.message.contains("user not found", ignoreCase = true)||
                            state.message.contains("has expired", ignoreCase = true)||
                            state.message.contains("auth credential is incorrect", ignoreCase = true)
                        ) {
                            Toast.makeText(this@LoginActivity, "User does not exist! Please register.", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                        } else {
                            Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }


    private fun setupClickListeners() {
        signInButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.loginUser(email, password)
        }

        createAccountText.setOnClickListener {
            // Navigate to registration screen
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        googleSignInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }

        facebookSignInButton.setOnClickListener {
            // Facebook sign in implementation would go here
            Toast.makeText(this, "Facebook sign in not implemented yet", Toast.LENGTH_SHORT).show()
        }

        forgotPasswordText.setOnClickListener {
            // Navigate to forgot password screen or show a dialog
            showForgotPasswordDialog()
        }
    }

    private fun navigateToMainScreen() {

        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showForgotPasswordDialog() {
        Toast.makeText(this, "Password reset functionality not implemented yet", Toast.LENGTH_SHORT).show()
    }


    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel()
            super.onBackPressed() // Only call super after second press
            finishAffinity()      // Finish the whole app
        } else {
            backToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT)
            backToast.show()
            backPressedTime = System.currentTimeMillis()
        }
    }
}