package com.majorproject.roomify.feature.home.presentaition.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.majorproject.roomify.MainActivity
import com.majorproject.roomify.R
import com.majorproject.roomify.core.data.RecentlyViewedManager
import com.majorproject.roomify.feature.ai_bot.presentation.screens.AiBot
import com.majorproject.roomify.feature.category.presentaition.adapter.HomeListAdapter
import com.majorproject.roomify.feature.category_detail.presentation.adapters.CategoryProductsAdapter
import com.majorproject.roomify.feature.category_detail.presentation.screen.CategoryDetailList
import com.majorproject.roomify.feature.common.adapters.CategoryProductsAdapter2
import com.majorproject.roomify.feature.common.adapters.ViewPagerAdapterHome
import com.majorproject.roomify.feature.furniture_list.presentation.screen.FurnitureListActivity
import com.majorproject.roomify.feature.home.presentaition.viewmodel.HomeViewModel
import com.majorproject.roomify.feature.product_detail.presentation.screens.ProductDetailActivity
import com.majorproject.roomify.feature.search.presentation.screens.SearchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class Home : Fragment() {

    private lateinit var rvCategory: RecyclerView
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var searchView: SearchView
    private lateinit var youraiButton: CardView
    private lateinit var viewinarbutton: TextView
    private lateinit var norecent: TextView
    private lateinit var moreCategories: Button
    private lateinit var viewall: Button

    private lateinit var rvRecentlyViewed: RecyclerView

    private val handler = Handler(Looper.getMainLooper())
    private val autoScrollInterval: Long = 3000 // 3 seconds
    private var currentPage = 0
    private var isAutoScrollActive = true
    @Inject
    lateinit var recentlyViewedManager: RecentlyViewedManager
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var categoryListAdapter: HomeListAdapter
    private lateinit var recentlyViewedAdapter: CategoryProductsAdapter2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.discount_sale_viewpager)
        tabLayout = view.findViewById(R.id.tab_layout)
        rvCategory = view.findViewById(R.id.rvCategory)
        searchView = view.findViewById(R.id.search_view)
        youraiButton = view.findViewById(R.id.yourAi)
        viewinarbutton = view.findViewById(R.id.viewInAr)
        norecent = view.findViewById(R.id.norecent)
        rvRecentlyViewed = view.findViewById(R.id.rvRecentlyViewed)
        moreCategories = view.findViewById(R.id.moreCategories)
        viewall = view.findViewById(R.id.viewall)

        youraiButton.setOnClickListener {
            // Use the new method to switch fragment and update bottom navigation
            (activity as MainActivity).switchToFragmentWithNav(
                AiBot(),
                MainActivity.ID_BOT
            )
        }
        moreCategories.setOnClickListener {
            // Use the new method to switch fragment and update bottom navigation
            (activity as MainActivity).switchToFragmentWithNav(
                CategoryDetailList(),
                MainActivity.ID_CATEGORY
            )
        }
        viewall.setOnClickListener {
            val intent = Intent(requireContext(), FurnitureListActivity::class.java)
            startActivity(intent)
        }
        viewinarbutton.setOnClickListener {

        }

        setupSearchView()
        setupViewPager()
        setupCategoryListAdapter()
        setupRecentlyViewedAdapter()

        // Add this line to fetch grid categories data
        viewModel.fetchGridCategories(100) // Fetch a reasonable number of categories

        observeCategories()
        loadRecentlyViewedProducts()
        startAutoScroll()
    }
    override fun onResume() {
        super.onResume()
        loadRecentlyViewedProducts() // Reload recently viewed products when fragment is resumed
    }
    // Add this method to your Home fragment class

    private fun setupSearchView() {
        val searchEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText?.apply {
            hint = "Search here..."
            setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            setHintTextColor(ContextCompat.getColor(requireContext(), R.color.LightGray))
            textSize = 14f
        }

        // Prevent typing directly in the search view on Home fragment
        searchView.isSubmitButtonEnabled = false
        searchEditText?.isFocusable = false
        searchEditText?.isClickable = true

        // Handle click on the search view to launch SearchActivity
        searchView.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        // Just in case, also handle submit actions
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    // Launch SearchActivity with the query
                    val intent = Intent(requireContext(), SearchActivity::class.java).apply {
                        putExtra("query", it)
                    }
                    startActivity(intent)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // We'll handle real-time search in SearchActivity
                return true
            }
        })
    }
    private fun setupRecentlyViewedAdapter() {
        recentlyViewedAdapter = CategoryProductsAdapter2 { product ->
            // Handle product click - navigate to product detail
            val intent = Intent(requireContext(), ProductDetailActivity::class.java).apply {
                putExtra("product", product)
            }
            startActivity(intent)
        }

        rvRecentlyViewed.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvRecentlyViewed.adapter = recentlyViewedAdapter
    }
    private fun loadRecentlyViewedProducts() {
        val recentlyViewed = recentlyViewedManager.getRecentlyViewedProducts()
        if (recentlyViewed.isNotEmpty()) {
            norecent.visibility=View.GONE
            recentlyViewedAdapter.updateProducts(recentlyViewed)
        } else {
            Log.d("Home", "No recently viewed products found")
        }
    }
    private fun setupViewPager() {
        val images = listOf(
            R.drawable.offer_poster0,
            R.drawable.offer_poster6,
            R.drawable.offer_poster4,
            R.drawable.offer_poster2
        )

        val titles = listOf(
            "Amazing Discounts Awaiting!",
            "Summer Sale!",
            "Exclusive Offers",
            "Festive Bonanza!"
        )

        val subtitles = listOf(
            "Up to 50%, Terms and conditions apply",
            "Save big on summer essentials",
            "Limited time only!",
            "Special deals just for you!"
        )

        viewPager.adapter = ViewPagerAdapterHome(images, titles, subtitles)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // You can customize tab if needed
        }.attach()
    }

    private fun setupCategoryListAdapter() {
        categoryListAdapter = HomeListAdapter(emptyList()) { category ->
            val bundle = Bundle().apply {
                putParcelable("category", category)
            }
            val detailFragment = CategoryDetailList()
            detailFragment.arguments = bundle

            // Use the new method to navigate to child fragment
            (activity as MainActivity).navigateToChildFragment(detailFragment)
        }

        rvCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvCategory.adapter = categoryListAdapter
    }

    private fun observeCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gridCategories.collectLatest { categories ->
                // Add logging
                Log.d("Home", "Received ${categories.size} categories")
                if (categories.isNotEmpty()) {
                    categoryListAdapter.updateList(categories)
                    // Add logging
                    Log.d("Home", "Updated adapter with ${categories.size} categories")
                } else {
                    Log.d("Home", "Received empty categories list")
                }
            }
        }
    }

    private fun startAutoScroll() {
        val runnable = object : Runnable {
            override fun run() {
                if (isAutoScrollActive) {
                    val itemCount = viewPager.adapter?.itemCount ?: 0
                    if (itemCount > 0) {
                        currentPage = (currentPage + 1) % itemCount
                        viewPager.setCurrentItem(currentPage, true)
                    }
                    handler.postDelayed(this, autoScrollInterval)
                }
            }
        }
        handler.postDelayed(runnable, autoScrollInterval)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    ViewPager2.SCROLL_STATE_DRAGGING -> isAutoScrollActive = false
                    ViewPager2.SCROLL_STATE_IDLE -> isAutoScrollActive = true
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}