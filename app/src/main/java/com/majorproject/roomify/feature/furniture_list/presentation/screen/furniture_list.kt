package com.majorproject.roomify.feature.furniture_list.presentation.screen

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.majorproject.roomify.R
import com.majorproject.roomify.core.di.ProductViewModel
import com.majorproject.roomify.feature.furniture_list.presentation.adapter.FurnitureLoadStateAdapter
import com.majorproject.roomify.feature.furniture_list.presentation.adapter.FurniturePagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FurnitureListActivity : AppCompatActivity() {
    private val vm: ProductViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var errorTextView: TextView
    private lateinit var fullScreenProgress: ProgressBar
    private lateinit var fullScreenError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_furniture_list)

        // View binding
        recyclerView      = findViewById(R.id.rvFurniture)
        errorTextView     = findViewById(R.id.errorTextView)
        fullScreenProgress= findViewById(R.id.fullScreenProgress)
        fullScreenError   = findViewById(R.id.fullScreenError)

        // 1) Adapter + footer
        val pagingAdapter = FurniturePagingAdapter()
        val footerAdapter = FurnitureLoadStateAdapter { pagingAdapter.retry() }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = pagingAdapter.withLoadStateFooter(footerAdapter)

        // 2) Initial/load‑error UI via loadStateFlow
        lifecycleScope.launchWhenStarted {
            pagingAdapter.loadStateFlow.collectLatest { loadStates ->
                when (val refresh = loadStates.refresh) {
                    is LoadState.Loading -> showInitialLoading()
                    is LoadState.Error   -> showInitialError(refresh.error)
                    else                  -> showContent()
                }
            }
        }

        // 3) Inline error message for empty or refresh errors
        pagingAdapter.addLoadStateListener { loadState ->
            when {
                loadState.refresh is LoadState.Error -> {
                    errorTextView.apply {
                        text       = (loadState.refresh as LoadState.Error)
                            .error.localizedMessage
                        visibility = View.VISIBLE
                    }
                }
                pagingAdapter.itemCount == 0 -> {
                    errorTextView.apply {
                        text       = "No items found"
                        visibility = View.VISIBLE
                    }
                }
                else -> errorTextView.visibility = View.GONE
            }
        }

        // 4) Collect data pages
        lifecycleScope.launchWhenStarted {
            vm.products.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }

    private fun showInitialLoading() {
        fullScreenProgress.visibility = View.VISIBLE
        fullScreenError.visibility    = View.GONE
        recyclerView.visibility       = View.GONE
        errorTextView.visibility      = View.GONE
    }

    private fun showInitialError(error: Throwable) {
        fullScreenProgress.visibility = View.GONE
        fullScreenError.apply {
            text       = "Error: ${error.localizedMessage}"
            visibility = View.VISIBLE
        }
        recyclerView.visibility  = View.GONE
        errorTextView.visibility = View.GONE
    }

    private fun showContent() {
        fullScreenProgress.visibility = View.GONE
        fullScreenError.visibility    = View.GONE
        recyclerView.visibility       = View.VISIBLE
    }
}
