package com.majorproject.roomify.feature.category_detail.presentation.adapters


import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.majorproject.roomify.R
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto

class CategoryProductsAdapter(
    private val onProductClick: (ProductDto) -> Unit
) : RecyclerView.Adapter<CategoryProductsAdapter.ProductViewHolder>() {

    private var products: List<ProductDto> = emptyList()

    fun updateProducts(newProducts: List<ProductDto>) {
        products = newProducts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onProductClick(product)
        }
    }

    override fun getItemCount(): Int = products.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        private val productName: TextView = itemView.findViewById(R.id.tvProductName)
        private val productPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        private val featuredIcon: ImageView = itemView.findViewById(R.id.ivFeatured)
        private val originalPriceText: TextView? = itemView.findViewById(R.id.tvOriginalPrice)

        fun bind(product: ProductDto) {
            productName.text = product.name

            // Handle discount price if available
            if (product.discountPrice > 0 && product.discountPrice < product.price) {
                // Show discounted price
                productPrice.text = "$${product.discountPrice}"

                // If we have the original price TextView in our layout
                if (originalPriceText != null) {
                    originalPriceText.visibility = View.VISIBLE
                    originalPriceText.text = "$${product.price}"
                    originalPriceText.paintFlags = originalPriceText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            } else {
                // Show regular price
                productPrice.text = "$${product.price}"

                // Hide original price if available
                originalPriceText?.visibility = View.GONE
            }

            featuredIcon.visibility = if (!product.featured) View.VISIBLE else View.GONE

            // Load image using Glide
            Glide.with(itemView.context)
                .load(product.imagePath)
                .placeholder(android.R.drawable.ic_menu_view)
                .error(R.drawable.searchicon)
                .into(productImage)
        }
    }
}