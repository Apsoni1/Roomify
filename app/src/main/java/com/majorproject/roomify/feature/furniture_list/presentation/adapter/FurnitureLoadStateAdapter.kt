package com.majorproject.roomify.feature.furniture_list.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.majorproject.roomify.R

class FurnitureLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<FurnitureLoadStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        private val retryButton: Button = view.findViewById(R.id.retryButton)

        fun bind(loadState: LoadState) {
            when (loadState) {
                is LoadState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    retryButton.visibility = View.GONE
                }
                is LoadState.Error -> {
                    progressBar.visibility = View.GONE
                    retryButton.visibility = View.VISIBLE
                }
                else -> {
                    progressBar.visibility = View.GONE
                    retryButton.visibility = View.GONE
                }
            }
            retryButton.setOnClickListener { retry() }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_load_state_footer, parent, false)
        return LoadStateViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: LoadStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }
}