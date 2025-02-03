package com.majorproject.roomify.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.majorproject.roomify.R

class ChooseLanguage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_language)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

    }
}