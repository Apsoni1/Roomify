package com.majorproject.roomify.feature.category.presentaition.screens

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.majorproject.roomify.R
import com.majorproject.roomify.feature.category.presentaition.viewmodel.CategoryViewModel
import com.majorproject.roomify.feature.common.adapters.CategoryViewPagerAdapter
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryScreen : Fragment() {

    private lateinit var viewPagerCategories: ViewPager2
    private lateinit var adapter: CategoryViewPagerAdapter
    private lateinit var dotsIndicator: DotsIndicator
    private lateinit var categoryTitle: TextView
    private val slideHandler = Handler()

    private val viewModel: CategoryViewModel by viewModels()
    private var categoryTitles: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        viewPagerCategories = view.findViewById(R.id.viewPagerCategories)
        dotsIndicator = view.findViewById(R.id.dots_indicator)
        categoryTitle = view.findViewById(R.id.categoryTitle)

        setupViewPager()

        viewModel.fetchCategories()

        lifecycleScope.launch {
            viewModel.categories.collectLatest { categories ->
                if (categories.isNotEmpty()) {
                    adapter = CategoryViewPagerAdapter(requireContext(), categories)
                    categoryTitles = categories.map { it.name }
                    viewPagerCategories.adapter = adapter
                    dotsIndicator.setViewPager2(viewPagerCategories)
                    updatePageTitle(viewPagerCategories.currentItem)
                }
            }
        }

        return view
    }

    private fun setupViewPager() {
        viewPagerCategories.setClipChildren(false)
        viewPagerCategories.setOffscreenPageLimit(3)
        viewPagerCategories.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        viewPagerCategories.setPageTransformer(transformer)

        viewPagerCategories.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                slideHandler.removeCallbacks(sliderRunnable)
                updatePageTitle(position)
            }
        })
    }

    private fun updatePageTitle(position: Int) {
        if (position in categoryTitles.indices) {
            categoryTitle.text = categoryTitles[position]
        }
    }

    private val sliderRunnable =
        Runnable { viewPagerCategories.setCurrentItem(viewPagerCategories.currentItem + 1) }
}
