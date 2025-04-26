package com.majorproject.roomify.feature.common.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import com.majorproject.roomify.R
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto

class CategoryViewPagerAdapter(
    private val context: Context,
    private val products: List<ProductDto>,
    private val onItemClick: (ProductDto) -> Unit = {}
) : RecyclerView.Adapter<CategoryViewPagerAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryImage: ImageView = view.findViewById(R.id.category_image)
        val progressBar: View = view.findViewById(R.id.category_progress_bar)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.viewpager_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val product = products[position]

        holder.progressBar.visibility = View.VISIBLE // Show ProgressBar

        Glide.with(context)
            .load(product.imagePath)
            .placeholder(R.drawable.info_outline_)
            .listener(object : com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE // Hide ProgressBar even if load fails
                    return false // Let Glide handle setting the error drawable
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE // Hide ProgressBar when loaded
                    return false // Let Glide handle setting the ImageView resource
                }
            })
            .into(holder.categoryImage)

        holder.itemView.setOnClickListener {
            onItemClick(product)
        }
    }

}