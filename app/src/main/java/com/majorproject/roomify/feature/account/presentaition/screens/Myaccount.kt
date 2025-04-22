package com.majorproject.roomify.feature.account.presentaition.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.majorproject.roomify.feature.about.presentaition.screens.AboutApp
import com.majorproject.roomify.feature.settings.presentaition.screens.ChooseLanguage
import com.majorproject.roomify.feature.settings.presentaition.screens.ChooseTheme
import com.majorproject.roomify.feature.support.presentaition.screens.CustomerSupport
import com.majorproject.roomify.feature.terms.presentaition.screens.TermsAndConditions
import com.majorproject.roomify.feature.orders.presentaition.screens.YourOrders
import com.majorproject.roomify.R


class Myaccount : Fragment() {

    private lateinit var dataPrefBtn: Button
    private lateinit var themeBtn: Button
    private lateinit var languageBtn: Button
    private lateinit var policiesBtn: Button
    private lateinit var supportBtn: Button
    private lateinit var aboutBtn: Button

    private lateinit var yourOrdersBtn: TextView
    private lateinit var buyAgainBtn: TextView
    private lateinit var yourAccountBtn: TextView
    private lateinit var yourListBtn: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_myaccount, container, false)
        // Inflate the layout for this fragment
        dataPrefBtn = view.findViewById(R.id.dateprefBtn)
        themeBtn = view.findViewById(R.id.themeBtn)
        languageBtn = view.findViewById(R.id.languageBtn)
        policiesBtn = view.findViewById(R.id.policiesBtn)
        supportBtn = view.findViewById(R.id.supportBtn)
        aboutBtn = view.findViewById(R.id.aboutBtn)

        yourOrdersBtn = view.findViewById(R.id.yourOrdersBtn)
        buyAgainBtn = view.findViewById(R.id.buyAgainBtn)
        yourAccountBtn = view.findViewById(R.id.yourAccountBtn)
        yourListBtn = view.findViewById(R.id.yourListBtn)




        dataPrefBtn.setOnClickListener {
        }

        themeBtn.setOnClickListener {
            val intent:Intent= Intent(requireContext(), ChooseTheme::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        languageBtn.setOnClickListener {
            val intent:Intent= Intent(requireContext(), ChooseLanguage::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        policiesBtn.setOnClickListener {
            val intent:Intent= Intent(requireContext(), TermsAndConditions::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        supportBtn.setOnClickListener {
            val intent:Intent= Intent(requireContext(), CustomerSupport::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        aboutBtn.setOnClickListener {
            val intent:Intent= Intent(requireContext(), AboutApp::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        yourOrdersBtn.setOnClickListener {
            val intent:Intent= Intent(requireContext(), YourOrders::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        buyAgainBtn.setOnClickListener {

        }

        yourAccountBtn.setOnClickListener {
            val intent:Intent= Intent(requireContext(), YourAccount::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        yourListBtn.setOnClickListener {

        }

        return view
    }

}