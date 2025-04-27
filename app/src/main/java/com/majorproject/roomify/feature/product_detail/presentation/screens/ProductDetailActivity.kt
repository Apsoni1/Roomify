package com.majorproject.roomify.feature.product_detail.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.majorproject.roomify.R
import com.majorproject.roomify.feature.ARViewer.presetation.ArViewerActivity
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import java.io.File
import java.text.NumberFormat
import java.util.Locale

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var productImageView: ImageView
    private lateinit var wishlistEmptyButton: ImageButton
    private lateinit var wishlistSelectedButton: ImageButton
    private lateinit var productNameTextView: TextView
    private lateinit var viewIn3DButton: Button
    private lateinit var discountPriceTextView: TextView
    private lateinit var originalPriceTextView: TextView
    private lateinit var shortDescriptionTextView: TextView
    private lateinit var woodTypeTextView: TextView
    private lateinit var finishTextView: TextView
    private lateinit var dimensionsTextView: TextView
    private lateinit var weightTextView: TextView
    private lateinit var stockStatusTextView: TextView
    private lateinit var tagsContainer: LinearLayout
    private lateinit var fullDescriptionTextView: TextView
    private lateinit var addToCartButton: Button
    private lateinit var arFragmentContainer: View
    val randomNumber = (1..4).random()

    private var isInWishlist = false
    private var currentProduct: ProductDto? = null
    private var isARViewActive = false

    // Firebase instances
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        window.statusBarColor = getColor(android.R.color.white)
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Get product data from intent
        val product = intent.getParcelableExtra<ProductDto>("product")
        currentProduct = product

        // Initialize views
        initViews()

        // Set product data if available
        product?.let {
            displayProductDetails(it)
        } ?: run {
            // Handle case where product is null
            Toast.makeText(this, "Error loading product details", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Set up click listeners
        setupClickListeners()
    }

    private fun initViews() {
        productImageView = findViewById(R.id.productImageView)
        wishlistEmptyButton = findViewById(R.id.wishlistEmpty)
        wishlistSelectedButton = findViewById(R.id.wishlistselected)
        productNameTextView = findViewById(R.id.productNameTextView)
        viewIn3DButton = findViewById(R.id.viewIn3DButton)
        discountPriceTextView = findViewById(R.id.discountPriceTextView)
        originalPriceTextView = findViewById(R.id.originalPriceTextView)
        shortDescriptionTextView = findViewById(R.id.shortDescriptionTextView)
        woodTypeTextView = findViewById(R.id.woodTypeTextView)
        finishTextView = findViewById(R.id.finishTextView)
        dimensionsTextView = findViewById(R.id.dimensionsTextView)
        weightTextView = findViewById(R.id.weightTextView)
        stockStatusTextView = findViewById(R.id.stockStatusTextView)
        tagsContainer = findViewById(R.id.tagsContainer)
        fullDescriptionTextView = findViewById(R.id.fullDescriptionTextView)
        addToCartButton = findViewById(R.id.addToCartButton)
        arFragmentContainer = findViewById(R.id.arFragmentContainer)

        // Initial wishlist state - hide selected heart
        wishlistSelectedButton.visibility = View.GONE
        arFragmentContainer.visibility = View.GONE
    }

    private fun displayProductDetails(product: ProductDto) {
        // Format currency with Rupee symbol
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "US"))

        // Set product name
        productNameTextView.text = product.name

        // Set prices
        discountPriceTextView.text = "${currencyFormat.format(product.discountPrice)} "
        originalPriceTextView.text = currencyFormat.format(product.price)
        originalPriceTextView.paintFlags = originalPriceTextView.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG

        // Set description (first line as short description)
        val shortDesc = product.description.split(".").firstOrNull() ?: product.description
        shortDescriptionTextView.text = shortDesc

        // Set full description
        fullDescriptionTextView.text = product.description

        // Set specifications
        woodTypeTextView.text = "Wood Type: ${product.woodType}"
        finishTextView.text = "Finish: ${product.finish}"

        // Set dimensions
        val dimensions = product.dimensions
        dimensionsTextView.text = "Dimensions: ${dimensions.depth}cm (L) x ${dimensions.width}cm (W) x ${dimensions.height}cm (H)"

        // Set weight
        weightTextView.text = "Weight: ${product.weight} kg"

        // Set stock status
        stockStatusTextView.text = if (product.stock > 0) "Availability: In Stock" else "Availability: Out of Stock"
        stockStatusTextView.setTextColor(resources.getColor(
            if (product.stock > 0) R.color.lightGreen else R.color.fadegrey,
            theme
        ))

        // Load product image using Glide
        Glide.with(this)
            .load(product.imagePath)
            .placeholder(R.drawable.sofa) // Use default image as placeholder
            .error(R.drawable.sofa) // Use default image if loading fails
            .into(productImageView)

        // Add tags if available
        product.tags?.let { tags ->
            displayTags(tags)
        }

        // Check if 3D model is available by name
        checkFor3DModel(product.category)
    }

    private fun checkFor3DModel(productName: String) {
        // Create a normalized product name for storage reference (remove spaces, lowercase)
        val normalizedName = productName.lowercase().replace("\\s+".toRegex(), "_")

        // We're hardcoding the test model path for now
        val storageRef = storage.reference.child("${normalizedName}_${randomNumber}.glb")

        Log.d("3DModelCheck", "Checking for model: ${normalizedName}_$randomNumber.glb")

        // Check if the 3D model exists in Firebase Storage
        storageRef.metadata.addOnSuccessListener {
            // 3D model exists for this product
            viewIn3DButton.isEnabled = true
            viewIn3DButton.alpha = 1.0f
            viewIn3DButton.text = "View in 3D"
        }.addOnFailureListener {
            // No 3D model available
            viewIn3DButton.isEnabled = false
            viewIn3DButton.alpha = 0.5f
            viewIn3DButton.text = "3D Model Not Available"
        }
    }

    private fun displayTags(tags: List<String>) {
        tagsContainer.removeAllViews()

        for (tag in tags) {
            val tagView = TextView(this).apply {
                text = "#$tag"
                textSize = 14f
                setTextColor(resources.getColor(R.color.Blue, theme))
                setPadding(16, 8, 16, 8)
            }
            tagsContainer.addView(tagView)
        }
    }



    private fun setupClickListeners() {
        // Wishlist button toggle
        wishlistEmptyButton.setOnClickListener {
            toggleWishlist()
        }

        wishlistSelectedButton.setOnClickListener {
            toggleWishlist()
        }

        // View in 3D button
        viewIn3DButton.setOnClickListener {
            if (!viewIn3DButton.isEnabled) {
                Toast.makeText(this, "3D model not available for this product", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            currentProduct?.let { product ->
                // Show loading indicator
                val loadingDialog = showLoadingDialog()

                // Create a normalized product name for storage reference
                val normalizedName = product.category.lowercase().replace("\\s+".toRegex(), "_")

                // Reference to the model in Firebase Storage
                val storageRef = storage.reference.child("${normalizedName}_$randomNumber.glb")
                Log.d("3DModelCheck", "Checking for model: ${normalizedName}_$randomNumber.glb")

                // Get download URL
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Dismiss loading dialog
                    loadingDialog.dismiss()

                    // Start the AR Activity with the download URL
                    val intent = Intent(this, ArViewerActivity::class.java).apply {
                        putExtra("productId", product.id)
                        putExtra("productName", product.name)
                        putExtra("modelUrl", uri.toString())
                    }
                    startActivity(intent)
                }.addOnFailureListener { exception ->
                    // Dismiss loading dialog
                    loadingDialog.dismiss()

                    // Handle error
                    Toast.makeText(
                        this,
                        "Failed to download model: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } ?: run {
                Toast.makeText(this, "Product data not available", Toast.LENGTH_SHORT).show()
            }
        }

        // Add to Cart button
        addToCartButton.setOnClickListener {
            if (isARViewActive) {
                // Don't process cart action if AR view is active
                return@setOnClickListener
            }
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
            // Implement actual cart logic here
        }
    }

    private fun showLoadingDialog(): androidx.appcompat.app.AlertDialog {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setView(R.layout.dialog_loading)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
        return dialog
    }



    private fun toggleWishlist() {
        // Only allow toggling wishlist when AR view is not active
        if (isARViewActive) return

        isInWishlist = !isInWishlist

        if (isInWishlist) {
            wishlistEmptyButton.visibility = View.GONE
            wishlistSelectedButton.visibility = View.VISIBLE
            Toast.makeText(this, "Added to wishlist", Toast.LENGTH_SHORT).show()

            // Add to Firestore wishlist collection
            currentProduct?.let { product ->
                addToWishlist(product)
            }
        } else {
            wishlistEmptyButton.visibility = View.VISIBLE
            wishlistSelectedButton.visibility = View.GONE
            Toast.makeText(this, "Removed from wishlist", Toast.LENGTH_SHORT).show()

            // Remove from Firestore wishlist collection
            currentProduct?.let { product ->
                removeFromWishlist(product)
            }
        }
    }

    private fun addToWishlist(product: ProductDto) {
        // Sample implementation - you'll need to adapt to your user management system
        // Normally you'd get the current user ID from your auth system
        val userId = "current_user_id"

        val wishlistItem = hashMapOf(
            "productId" to product.id,
            "userId" to userId,
            "dateAdded" to com.google.firebase.Timestamp.now()
        )

        db.collection("wishlists")
            .add(wishlistItem)
            .addOnSuccessListener { documentReference ->
                // Successfully added to wishlist
            }
            .addOnFailureListener { e ->
                // Handle failure
                Toast.makeText(this, "Failed to add to wishlist: ${e.message}", Toast.LENGTH_SHORT).show()
                // Revert the UI state
                isInWishlist = false
                wishlistEmptyButton.visibility = View.VISIBLE
                wishlistSelectedButton.visibility = View.GONE
            }
    }

    private fun removeFromWishlist(product: ProductDto) {
        // Sample implementation
        val userId = "current_user_id"

        db.collection("wishlists")
            .whereEqualTo("productId", product.id)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.delete()
                }
            }
            .addOnFailureListener { e ->
                // Handle failure
                Toast.makeText(this, "Failed to remove from wishlist: ${e.message}", Toast.LENGTH_SHORT).show()
                // Revert the UI state
                isInWishlist = true
                wishlistEmptyButton.visibility = View.GONE
                wishlistSelectedButton.visibility = View.VISIBLE
            }
    }

    override fun onBackPressed() {
        // Handle back press when AR view is active
        if (isARViewActive) {
            supportFragmentManager.popBackStack()
            arFragmentContainer.visibility = View.GONE
            isARViewActive = false
        } else {
            super.onBackPressed()
        }
    }
}