package com.majorproject.roomify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.majorproject.roomify.feature.cart.presentaition.screens.Cart
import com.majorproject.roomify.feature.home.presentaition.screens.Home
import com.majorproject.roomify.feature.account.presentaition.screens.Myaccount
import dagger.hilt.android.AndroidEntryPoint
import com.majorproject.roomify.feature.category.presentaition.screens.CategoryScreen as Category
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var chipNavigationBar: ChipNavigationBar
    private lateinit var active: Fragment
    private lateinit var home: Home
    private lateinit var myAccount: Myaccount
    private lateinit var category: Category
    private lateinit var cart: Cart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         chipNavigationBar = findViewById(R.id.chipNavigationBottomBar)

        home = Home()
        myAccount = Myaccount()
        category = Category()
        cart = Cart()


        if (savedInstanceState == null) {
             supportFragmentManager.beginTransaction()
                .add(R.id.mainframeLayout, home, "home")
                .commit()
            active = home
        } else {
            // Restore the active fragment from saved state
            active = supportFragmentManager.findFragmentByTag("home") ?: home
        }

        chipNavigationBar.setItemSelected(R.id.home, true)

        chipNavigationBar.setOnItemSelectedListener {
            when (it) {
                R.id.home -> loadFragment(home, "home")
                R.id.category -> loadFragment(category, "category")
                R.id.cart -> loadFragment(cart, "cart")
                R.id.account -> loadFragment(myAccount, "myaccount")
            }
        }

    }
    fun loadAllFragmentsAtFirst(){
        active=home
        supportFragmentManager.beginTransaction().add(R.id.mainframeLayout,home,"home").hide(home).commit()
        supportFragmentManager.beginTransaction().add(R.id.mainframeLayout,myAccount,"myaccount").hide(home).commit()
        supportFragmentManager.beginTransaction().add(R.id.mainframeLayout,category,"category").hide(category).commit()
        supportFragmentManager.beginTransaction().add(R.id.mainframeLayout,cart,"cart").hide(cart).commit()
        chipNavigationBar.setItemSelected(R.id.home)


    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        if (fragment.isAdded) {
            // If the fragment is already added, just show it
            transaction.hide(active).show(fragment)
        } else {
            // Otherwise, add it and show it
            transaction.hide(active).add(R.id.mainframeLayout, fragment, tag)
        }
        transaction.commit()
        active = fragment
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}