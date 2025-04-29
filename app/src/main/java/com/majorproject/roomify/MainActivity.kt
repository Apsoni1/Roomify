package com.majorproject.roomify

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.majorproject.roomify.feature.ai_bot.presentation.screens.AiBot
import com.majorproject.roomify.feature.account.presentaition.screens.Myaccount
import com.majorproject.roomify.feature.category.presentaition.screens.CategoryScreen as Category
import com.majorproject.roomify.feature.category_detail.presentation.screen.CategoryDetailList
import com.majorproject.roomify.feature.home.presentaition.screens.Home
import com.nafis.bottomnavigation.NafisBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigation: NafisBottomNavigation
    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast
    private val home = Home()
    private val category = Category()
    private val aibot = AiBot()
    private val account = Myaccount()

    private var active: Fragment = home

    companion object {
        const val ID_HOME = 1
        const val ID_CATEGORY = 2
        const val ID_BOT = 3
        const val ID_ACCOUNT = 4
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
        bottomNavigation.add(NafisBottomNavigation.Model(ID_BOT, R.drawable.bot))
        bottomNavigation.add(NafisBottomNavigation.Model(ID_ACCOUNT, R.drawable.myaccount))

        if (savedInstanceState == null) {
            loadAllFragments()
        }

        bottomNavigation.setOnShowListener {
            when (it.id) {
                ID_HOME -> switchFragment(home)
                ID_CATEGORY -> switchFragment(category)
                ID_BOT -> switchFragment(aibot)
                ID_ACCOUNT -> switchFragment(account)
            }
        }
        bottomNavigation.show(ID_HOME)
    }

    private fun loadAllFragments() {
        supportFragmentManager.beginTransaction()
            .add(R.id.mainframeLayout, home, "home")
            .add(R.id.mainframeLayout, category, "category").hide(category)
            .add(R.id.mainframeLayout, aibot, "bot").hide(aibot)
            .add(R.id.mainframeLayout, account, "account").hide(account)
            .commit()
        active = home
    }

    fun switchFragment(target: Fragment) {
        if (target != active) {
            supportFragmentManager.beginTransaction()
                .hide(active)
                .show(target)
                .commit()
            active = target
        }
    }

    // New method to update both fragment and bottom navigation
    fun switchToFragmentWithNav(target: Fragment, navId: Int) {
        switchFragment(target)
        bottomNavigation.show(navId)
    }

    // Method to handle navigation to child fragments (like CategoryDetailList)
    fun navigateToChildFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.mainframeLayout, fragment)
            .hide(active)
            .addToBackStack(null)
            .commit()
        active = fragment
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            // We have fragments in the back stack
            supportFragmentManager.popBackStack()

            // Find the current visible fragment
            val fragments = supportFragmentManager.fragments
            for (fragment in fragments) {
                if (fragment.isVisible) {
                    active = fragment
                    updateBottomNavFromFragment(fragment)
                    break
                }
            }
        } else if (active != home) {
            // If we're on a main tab but not home, go back to home
            switchToFragmentWithNav(home, ID_HOME)
        } else {
            // Handle exit with double back press
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel()
                super.onBackPressed()
                finishAffinity()
            } else {
                backToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT)
                backToast.show()
                backPressedTime = System.currentTimeMillis()
            }
        }
    }

    // Helper method to update bottom navigation based on the current fragment
    private fun updateBottomNavFromFragment(fragment: Fragment) {
        when (fragment) {
            is Home -> bottomNavigation.show(ID_HOME)
            is Category -> bottomNavigation.show(ID_CATEGORY)
            is AiBot -> bottomNavigation.show(ID_BOT)
            is Myaccount -> bottomNavigation.show(ID_ACCOUNT)
            // Don't update nav for child fragments - keep parent tab selected
        }
    }
}