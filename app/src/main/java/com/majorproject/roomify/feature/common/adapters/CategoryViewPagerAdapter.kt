package com.majorproject.roomify.feature.common.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.majorproject.roomify.R

class CategoryViewPagerAdapter(
    private val context: Context,
    private val images: List<Int> // List of drawable resource IDs
) : RecyclerView.Adapter<CategoryViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryImage: ImageView = itemView.findViewById(R.id.category_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.viewpager_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            // Use Glide to load the drawable resource ID
            Glide.with(context)
                .load(images[position])
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.categoryImage)
        } catch (ignore: Exception) {
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}
