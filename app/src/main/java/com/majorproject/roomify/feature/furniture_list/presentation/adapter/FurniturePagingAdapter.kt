package com.majorproject.roomify.feature.furniture_list.presentation.adapter


import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
        private val description: TextView = view.findViewById(R.id.tvDescription)
        private val price: TextView = view.findViewById(R.id.tvPrice)
        private val discountprice: TextView = view.findViewById(R.id.tvDiscountPrice)
        private val image: ImageView = view.findViewById(R.id.ivFurniture)
        private val imageProgress: ProgressBar = view.findViewById(R.id.imageProgress)

        fun bind(item: ProductDto?) {
            if (item == null) return

            name.text = item.name
            description.text= item.description
            val originalPrice = "$${item.price}"
            val spannable = android.text.SpannableString(originalPrice)
            spannable.setSpan(object : android.text.style.StrikethroughSpan() {
                override fun updateDrawState(ds: android.text.TextPaint) {
                    super.updateDrawState(ds)
                    ds.strokeWidth = 3f  // Increases the strike-through thickness
                }
            }, 0, originalPrice.length, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            price.text = spannable

            discountprice.text = "$${item.discountPrice}"
            imageProgress.visibility = View.VISIBLE
            Glide.with(image.context)
                .load(item.imagePath)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageProgress.visibility = View.GONE
                        return false
                    }


                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageProgress.visibility = View.GONE
                        return false                    }
                })

                .into(image)
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
