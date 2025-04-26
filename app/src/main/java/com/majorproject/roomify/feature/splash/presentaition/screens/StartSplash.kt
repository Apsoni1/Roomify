package com.majorproject.roomify.feature.splash.presentaition.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.majorproject.roomify.R
import com.majorproject.roomify.MainActivity
import com.majorproject.roomify.feature.auth.presentation.view.LoginActivity
import com.majorproject.roomify.feature.auth.presentation.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StartSplash : AppCompatActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_splash)

        // Set up the window appearance
        setupWindowAppearance()

        // Start the splash screen timer and check authentication
        checkAuthAndNavigate()
    }

    private fun setupWindowAppearance() {
        val window = window
        val background = resources.getDrawable(R.drawable.bg_gradiantop, theme)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.transprant, theme)
        window.navigationBarColor = resources.getColor(R.color.transprant, theme)
        window.setBackgroundDrawable(background)
    }

    private fun checkAuthAndNavigate() {
        lifecycleScope.launch {
            // Delay for splash screen display
            delay(2000)

            // Check if user is already logged in using injected FirebaseAuth
            val currentUser = firebaseAuth.currentUser

            if (currentUser != null) {
                // User is logged in, navigate to main activity
                navigateToMainActivity()
            } else {
                // User is not logged in, navigate to login activity
                navigateToLoginActivity()
            }
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this@StartSplash, MainActivity::class.java))
        finish()
    }

    private fun navigateToLoginActivity() {
        startActivity(Intent(this@StartSplash, LoginActivity::class.java))
        finish()
    }
}