package com.majorproject.roomify.feature.category.presentaition.screens

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.majorproject.roomify.feature.common.adapters.CategoryViewPagerAdapter
import com.majorproject.roomify.R
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class Category : Fragment() {

    private lateinit var viewPagerCategories: ViewPager2
    private lateinit var adapter: CategoryViewPagerAdapter
    private lateinit var dotsIndicator: DotsIndicator
    private lateinit var categoryTitle: TextView
    private var slideHandler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        viewPagerCategories = view.findViewById(R.id.viewPagerCategories)
        dotsIndicator = view.findViewById(R.id.dots_indicator)
        categoryTitle = view.findViewById(R.id.categoryTitle)  // Find the TextView for title

        val images = listOf(
            R.drawable.recliner,
            R.drawable.img_1,
            R.drawable.img_4,
            R.drawable.img_3
        )

        adapter = CategoryViewPagerAdapter(requireContext(), images)
        viewPagerCategories.adapter = adapter
        viewPagerCategories.setClipChildren(false)
        viewPagerCategories.setOffscreenPageLimit(3)
        viewPagerCategories.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER)
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - Math.abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        viewPagerCategories.setPageTransformer(compositePageTransformer)
        viewPagerCategories.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                slideHandler.removeCallbacks(sliderRunnable)
                updatePageTitle(position)  // Update the page title based on position
            }
        })
        dotsIndicator.setViewPager2(viewPagerCategories)
        return view
    }

    private fun updatePageTitle(position: Int) {
        val titles = listOf("Recliner", "ottoman", "Cupboard","Sofa") // Titles for each page
        if (position in titles.indices) {
            categoryTitle.text = titles[position]

        }
    }

    private val sliderRunnable =
        Runnable { viewPagerCategories.setCurrentItem(viewPagerCategories.currentItem + 1) }
}
