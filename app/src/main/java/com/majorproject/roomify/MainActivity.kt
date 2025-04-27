package com.majorproject.roomify

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.majorproject.roomify.feature.ai_bot.presentation.screens.AiBot
import com.majorproject.roomify.feature.account.presentaition.screens.Myaccount
import com.majorproject.roomify.feature.category.presentaition.screens.CategoryScreen as Category
import com.majorproject.roomify.feature.home.presentaition.screens.Home
import com.majorproject.roomify.feature.category_detail.presentation.screen.CategoryDetailList// ← add this!
import com.nafis.bottomnavigation.NafisBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigation: NafisBottomNavigation
    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast
    private val home     = Home()
    private val category = Category()
    private val aibot    = AiBot()     // ← lowercase variable
    private val account  = Myaccount()

    private var active: Fragment = home

    companion object {
        private const val ID_HOME     = 1
        private const val ID_CATEGORY = 2
        private const val ID_BOT      = 3
        private const val ID_ACCOUNT  = 4
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
                ID_HOME     -> switchFragment(home)
                ID_CATEGORY -> switchFragment(category)
                ID_BOT      -> switchFragment(aibot)
                ID_ACCOUNT  -> switchFragment(account)
            }
        }
        bottomNavigation.show(ID_HOME)

        // … rest of your code …
    }

    private fun loadAllFragments() {
        supportFragmentManager.beginTransaction()
            .add(R.id.mainframeLayout, home, "home")
            .add(R.id.mainframeLayout, category, "category").hide(category)
            .add(R.id.mainframeLayout, aibot, "bot").hide(aibot)       // ← use aibot here
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
