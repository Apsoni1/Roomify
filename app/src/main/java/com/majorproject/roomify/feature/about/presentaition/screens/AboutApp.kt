package com.majorproject.roomify.feature.about.presentaition.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.majorproject.roomify.R

class AboutApp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

    }

}