package com.majorproject.roomify.feature.account.presentaition.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.majorproject.roomify.R
import com.majorproject.roomify.feature.about.presentaition.screens.AboutApp
import com.majorproject.roomify.feature.orders.presentaition.screens.YourOrders
import com.majorproject.roomify.feature.settings.presentaition.screens.ChooseLanguage
import com.majorproject.roomify.feature.support.presentaition.screens.CustomerSupport

class Myaccount : Fragment() {

    private lateinit var languageBtn: TextView
    private lateinit var supportBtn: TextView
    private lateinit var aboutBtn: TextView
    private lateinit var viewCartBtn: TextView
    private lateinit var ordersBtn: TextView
    private lateinit var wishlistBtn: TextView
    private lateinit var logoutBtn: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_myaccount, container, false)

        // initialize views
        languageBtn = view.findViewById(R.id.changeLanguage)
        supportBtn = view.findViewById(R.id.support)
        aboutBtn = view.findViewById(R.id.about)
        viewCartBtn = view.findViewById(R.id.ViewCart)
        ordersBtn = view.findViewById(R.id.orders)
        wishlistBtn = view.findViewById(R.id.Wishlist)
        logoutBtn = view.findViewById(R.id.logout)

        // set click listeners
        languageBtn.setOnClickListener {
            startActivity(Intent(requireContext(), ChooseLanguage::class.java))
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        supportBtn.setOnClickListener {
            startActivity(Intent(requireContext(), CustomerSupport::class.java))
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        aboutBtn.setOnClickListener {
            startActivity(Intent(requireContext(), AboutApp::class.java))
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        ordersBtn.setOnClickListener {
            startActivity(Intent(requireContext(), YourOrders::class.java))
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // You can similarly add for viewCartBtn, wishlistBtn, logoutBtn if needed.

        return view
    }
}
