package com.majorproject.roomify.feature.furniture_list.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.majorproject.roomify.R
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto

class FurniturePagingAdapter :
    PagingDataAdapter<ProductDto, FurniturePagingAdapter.FurnitureViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductDto>() {
            override fun areItemsTheSame(a: ProductDto, b: ProductDto) = a.id == b.id
            override fun areContentsTheSame(a: ProductDto, b: ProductDto) = a == b
        }
    }

    inner class FurnitureViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.tvName)
        private val price: TextView = view.findViewById(R.id.tvPrice)
        private val image: ImageView = view.findViewById(R.id.ivFurniture)

        fun bind(item: ProductDto?) {
            item?.let {
                name.text = it.name
                price.text = "$${it.discountPrice}"
                Glide.with(image).load(it.imagePath).into(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnitureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_furniture, parent, false)
        return FurnitureViewHolder(view)
    }

    override fun onBindViewHolder(holder: FurnitureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}