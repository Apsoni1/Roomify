package com.majorproject.roomify.feature.support.presentaition.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.majorproject.roomify.R

class CustomerSupport : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_support)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

    }

}