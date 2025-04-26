package com.majorproject.roomify.feature.common.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.majorproject.roomify.R
import com.majorproject.roomify.feature.category.domain.model.Category

class CategoryViewPagerAdapter(
    private val context: Context,
    private val categories: List<Category>
) : RecyclerView.Adapter<CategoryViewPagerAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryImage: ImageView = view.findViewById(R.id.category_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.viewpager_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        Glide.with(context)
            .load(category.imagePath)
            .placeholder(R.drawable.info_outline_)
            .into(holder.categoryImage)
    }
}

