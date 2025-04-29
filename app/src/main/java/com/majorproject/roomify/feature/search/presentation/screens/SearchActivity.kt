package com.majorproject.roomify.feature.search.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.majorproject.roomify.R
import com.majorproject.roomify.feature.category_detail.presentation.adapters.CategoryProductsAdapter
import com.majorproject.roomify.feature.common.adapters.CategoryProductsAdapter2
import com.majorproject.roomify.feature.product_detail.presentation.screens.ProductDetailActivity
import com.majorproject.roomify.feature.search.presentation.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var rvSearchResults: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var noResultsText: TextView
    private lateinit var backButton: ImageView

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchResultsAdapter: CategoryProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        window.statusBarColor = getColor(android.R.color.white)
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Initialize views
        searchView = findViewById(R.id.search_view)
        rvSearchResults = findViewById(R.id.rv_search_results)
        progressBar = findViewById(R.id.progress_bar)
        noResultsText = findViewById(R.id.tv_no_results)
        backButton = findViewById(R.id.back_button)

        setupSearchView()
        setupRecyclerView()
        setupObservers()
        handleIntentQuery()

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupSearchView() {
        val searchEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText?.apply {
            hint = "Search furniture..."
            setTextColor(ContextCompat.getColor(this@SearchActivity, R.color.black))
            setHintTextColor(ContextCompat.getColor(this@SearchActivity, R.color.LightGray))
            textSize = 14f
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchProducts(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank() && newText.length >= 2) {
                    searchProducts(newText)
                }
                return true
            }
        })

        // Auto-focus and open keyboard
        searchView.requestFocus()
        searchView.isIconified = false
    }

    private fun setupRecyclerView() {
        searchResultsAdapter = CategoryProductsAdapter { product ->
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra("product", product)
            }
            startActivity(intent)
        }

        rvSearchResults.apply {
            layoutManager = GridLayoutManager(this@SearchActivity, 2)
            adapter = searchResultsAdapter
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.searchResults.collectLatest { products ->
                searchResultsAdapter.updateProducts(products)

                if (products.isEmpty() && !viewModel.isSearching.value) {
                    noResultsText.visibility = View.VISIBLE
                } else {
                    noResultsText.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launch {
            viewModel.isSearching.collectLatest { isSearching ->
                progressBar.visibility = if (isSearching) View.VISIBLE else View.GONE

                if (!isSearching && viewModel.searchResults.value.isEmpty()) {
                    noResultsText.visibility = View.VISIBLE
                } else {
                    noResultsText.visibility = View.GONE
                }
            }
        }
    }

    private fun searchProducts(query: String) {
        viewModel.searchProducts(query)
    }

    private fun handleIntentQuery() {
        intent.getStringExtra("query")?.let { query ->
            searchView.setQuery(query, true)
        }
    }
}