package com.majorproject.roomify.feature.category.presentaition.screens

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.majorproject.roomify.R
import com.majorproject.roomify.feature.category.domain.model.Category
import com.majorproject.roomify.feature.category.presentaition.adapter.CategoryListAdapter
import com.majorproject.roomify.feature.category.presentaition.viewmodel.CategoryViewModel
import com.majorproject.roomify.feature.category_detail.presentation.screen.CategoryDetailList
import com.majorproject.roomify.feature.common.adapters.CategoryViewPagerAdapter
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import com.majorproject.roomify.feature.product_detail.presentation.screens.ProductDetailActivity
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryScreen : Fragment() {
    private lateinit var rvCategory: RecyclerView
    private lateinit var viewPagerCategories: ViewPager2
    private lateinit var adapter: CategoryViewPagerAdapter
    private lateinit var dotsIndicator: DotsIndicator
    private lateinit var categoryTitle: TextView
    private val slideHandler = Handler()
    private lateinit var categoryListAdapter: CategoryListAdapter
    private lateinit var progressBar: ProgressBar

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
        rvCategory = view.findViewById(R.id.rvCategory)

        categoryListAdapter = CategoryListAdapter(emptyList()) { category ->
            val bundle = Bundle().apply {
                putParcelable("category", category)
            }
            val detailFragment = CategoryDetailList()
            detailFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .add(R.id.mainframeLayout, detailFragment)
                .hide(this@CategoryScreen)
                .addToBackStack(null)
                .commit()
        }

        rvCategory.layoutManager = GridLayoutManager(requireContext(), 2)
        rvCategory.adapter = categoryListAdapter
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        setupViewPager()

        viewModel.fetchViewPagerCategories()
        viewModel.fetchGridCategories(limit = 100)
        viewModel.fetchSecondItemsOfEachCategory(limit = 100)

        // Initialize adapter with empty list and click handler
        adapter = CategoryViewPagerAdapter(
            requireContext(),
            emptyList()
        ) { product ->
            navigateToProductDetail(product)
        }
        viewPagerCategories.adapter = adapter

        // Collect second items for the ViewPager
        lifecycleScope.launch {
            viewModel.secondItems.collectLatest { products ->
                if (products.isNotEmpty()) {
                    adapter = CategoryViewPagerAdapter(
                        requireContext(),
                        products
                    ) { product ->
                        navigateToProductDetail(product)
                    }

                    // Extract category names from products for title display
                    categoryTitles = products.map { it.category ?: "Unknown" }
                    viewPagerCategories.adapter = adapter
                    dotsIndicator.setViewPager2(viewPagerCategories)
                    updatePageTitle(viewPagerCategories.currentItem)
                    progressBar.visibility = View.GONE
                }
            }
        }

        // Collect categories for the grid
        lifecycleScope.launch {
            viewModel.gridCategories.collectLatest { categories ->
                if (categories.isNotEmpty()) {
                    categoryListAdapter.updateList(categories)
                    progressBar.visibility = View.GONE
                }
            }
        }

        return view
    }

    private fun navigateToProductDetail(product: ProductDto) {
        val intent = Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra("product", product)
        }
        startActivity(intent)
    }

    private fun setupViewPager() {
        viewPagerCategories.clipChildren = false
        viewPagerCategories.offscreenPageLimit = 3
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
        Runnable { viewPagerCategories.currentItem += 1 }
}
