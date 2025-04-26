package com.majorproject.roomify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.majorproject.roomify.feature.account.presentaition.screens.Myaccount
import com.majorproject.roomify.feature.cart.presentaition.screens.Cart
import com.majorproject.roomify.feature.category_detail.presentation.screen.CategoryDetailList
import com.majorproject.roomify.feature.category.presentaition.screens.CategoryScreen as Category
import com.majorproject.roomify.feature.home.presentaition.screens.Home
import com.nafis.bottomnavigation.NafisBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: NafisBottomNavigation

    private val home = Home()
    private val category = Category()
    private val cart = Cart()
    private val account = Myaccount()

    private var active: Fragment = home

    companion object {
        private const val ID_HOME = 1
        private const val ID_CATEGORY = 2
        private const val ID_CART = 3
        private const val ID_ACCOUNT = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor = getColor(android.R.color.white)
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        bottomNavigation = findViewById(R.id.bottomNavigation)

        bottomNavigation.add(NafisBottomNavigation.Model(ID_HOME, R.drawable.homeicon))
        bottomNavigation.add(NafisBottomNavigation.Model(ID_CATEGORY, R.drawable.categoryicon))
        bottomNavigation.add(NafisBottomNavigation.Model(ID_CART, R.drawable.carticon))
        bottomNavigation.add(NafisBottomNavigation.Model(ID_ACCOUNT, R.drawable.myaccount))

        if (savedInstanceState == null) {
            loadAllFragments()
        }

        bottomNavigation.setOnShowListener {
            when (it.id) {
                ID_HOME -> switchFragment(home)
                ID_CATEGORY -> switchFragment(category)
                ID_CART -> switchFragment(cart)
                ID_ACCOUNT -> switchFragment(account)
            }
        }

        bottomNavigation.show(ID_HOME)
    }

    private fun loadAllFragments() {
        supportFragmentManager.beginTransaction()
            .add(R.id.mainframeLayout, home, "home")
            .add(R.id.mainframeLayout, category, "category").hide(category)
            .add(R.id.mainframeLayout, cart, "cart").hide(cart)
            .add(R.id.mainframeLayout, account, "account").hide(account)
            .commit()
        active = home
    }

    private fun switchFragment(target: Fragment) {
        if (target != active) {
            supportFragmentManager.beginTransaction()
                .hide(active)
                .show(target)
                .commit()
            active = target
        }
    }


    override fun onBackPressed() {

        val currentFragment = supportFragmentManager.findFragmentById(R.id.mainframeLayout)
        if (currentFragment is CategoryDetailList && active == category) {

            super.onBackPressed()
            return
        }


        if (active != home) {
            bottomNavigation.show(ID_HOME)
        } else {
            super.onBackPressed()
        }
    }
}
