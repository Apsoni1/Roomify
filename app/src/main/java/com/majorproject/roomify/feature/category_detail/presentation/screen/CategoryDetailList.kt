package com.majorproject.roomify.feature.category_detail.presentation.screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.majorproject.roomify.R
import com.majorproject.roomify.feature.category.domain.model.Category
import com.majorproject.roomify.feature.category_detail.presentation.adapters.CategoryProductsAdapter
import com.majorproject.roomify.feature.category_detail.presentation.viewmodel.CategoryDetailViewModel
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import com.majorproject.roomify.feature.product_detail.presentation.screens.ProductDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryDetailList : Fragment() {
    private val viewModel: CategoryDetailViewModel by viewModels()
    private lateinit var rvCategoryProducts: RecyclerView
    private lateinit var categoryTitle: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyState: TextView
    private lateinit var productsAdapter: CategoryProductsAdapter
    private var category: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get category from arguments
        category = arguments?.getParcelable("category")

        // Handle system back press
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_detail_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        rvCategoryProducts = view.findViewById(R.id.rvCategoryProducts)
        categoryTitle = view.findViewById(R.id.categoryDetailTitle)
        progressBar = view.findViewById(R.id.categoryDetailProgressBar)
        tvEmptyState = view.findViewById(R.id.tvEmptyState)

        // Set category title
        categoryTitle.text = category?.name ?: "Category Products"

        // Setup RecyclerView
        setupRecyclerView()

        // Setup observers
        setupObservers()

        // Fetch products for this category
        category?.name?.let { categoryName ->
            viewModel.fetchProductsByCategory(categoryName)
        }
    }

    private fun setupRecyclerView() {
        productsAdapter = CategoryProductsAdapter { product ->
            navigateToProductDetail(product)
        }

        rvCategoryProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productsAdapter
        }
    }

    private fun setupObservers() {
        // Observe loading state
        lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

                // Hide empty state while loading
                if (isLoading) {
                    tvEmptyState.visibility = View.GONE
                }
            }
        }

        // Observe products
        lifecycleScope.launch {
            viewModel.categoryProducts.collectLatest { products ->
                productsAdapter.updateProducts(products)

                // Show empty state if no products and not loading
                if (products.isEmpty() && !viewModel.isLoading.value) {
                    tvEmptyState.visibility = View.VISIBLE
                    rvCategoryProducts.visibility = View.GONE
                } else {
                    tvEmptyState.visibility = View.GONE
                    rvCategoryProducts.visibility = View.VISIBLE
                }
            }
        }

        // Observe errors
        lifecycleScope.launch {
            viewModel.error.collectLatest { errorMessage ->
                errorMessage?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    // Show empty state on error
                    if (viewModel.categoryProducts.value.isEmpty()) {
                        tvEmptyState.text = "Error: $it"
                        tvEmptyState.visibility = View.VISIBLE
                        rvCategoryProducts.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun navigateToProductDetail(product: ProductDto) {
        val intent = Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra("product", product)
        }
        startActivity(intent)
    }
}