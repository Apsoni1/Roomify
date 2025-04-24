package com.majorproject.roomify.feature.furniture_list.presentation.adapter

import android.graphics.pdf.LoadParams
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.majorproject.roomify.R
import com.majorproject.roomify.core.di.ApiService
import com.majorproject.roomify.core.di.ProductDto
import retrofit2.HttpException

class FurniturePagingAdapter :
    PagingDataAdapter<ProductDto, FurniturePagingAdapter.Holder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductDto>() {
            override fun areItemsTheSame(a: ProductDto, b: ProductDto) = a.id == b.id
            override fun areContentsTheSame(a: ProductDto, b: ProductDto) = a == b
        }
    }

    inner class Holder(view: View): RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.tvName)
        private val price: TextView= view.findViewById(R.id.tvPrice)
        private val image: ImageView = view.findViewById(R.id.ivFurniture)

        fun bind(item: ProductDto?) {
            item?.let {
                name.text = it.name
                price.text = "$${it.discountPrice}"
                Glide.with(image).load(it.imagePath).into(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= Holder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_furniture, parent, false)
    )
    override fun onBindViewHolder(holder: Holder, pos: Int) =
        holder.bind(getItem(pos))
}


class ProductPagingSource(
    private val api: ApiService
): PagingSource<Int, ProductDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductDto> {
        val page = params.key ?: 1
        val pageSize = params.loadSize
        return try {
            val resp = api.fetchProductsPage(page, pageSize)
            if (!resp.isSuccessful) throw HttpException(resp)

            val items = resp.body()?.data ?: emptyList()
            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.size < pageSize) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductDto>): Int? {
        return state.anchorPosition
            ?.let { pos -> state.closestPageToPosition(pos) }
            ?.prevKey
            ?.plus(1)
            ?: state.closestPageToPosition(state.anchorPosition ?: 0)
                ?.nextKey
                ?.minus(1)
    }
}

class FurnitureLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<FurnitureLoadStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val progress = view.findViewById<ProgressBar>(R.id.progressBar)
        private val retryBtn = view.findViewById<Button>(R.id.retryButton)

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Loading) {
                progress.visibility = View.VISIBLE
                retryBtn.visibility = View.GONE
            } else {
                progress.visibility = View.GONE
                retryBtn.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
            }
            retryBtn.setOnClickListener { retry() }
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

