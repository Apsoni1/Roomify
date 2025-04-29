package com.majorproject.roomify.core.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.majorproject.roomify.feature.furniture_list.data.dto.ProductDto
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager class for handling recently viewed products
 * Uses SharedPreferences to persist the data
 */
@Singleton
class RecentlyViewedManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    /**
     * Add a product to recently viewed list
     * If product already exists, it will be moved to the top of the list
     */
    fun addToRecentlyViewed(product: ProductDto) {
        val currentList = getRecentlyViewedProducts().toMutableList()

        // Remove if already exists (to reorder it to the top)
        currentList.removeAll { it.id == product.id }

        // Add at the beginning
        currentList.add(0, product)

        // Limit the size to MAX_ITEMS
        val limitedList = if (currentList.size > MAX_ITEMS) {
            currentList.subList(0, MAX_ITEMS)
        } else {
            currentList
        }

        // Save to SharedPreferences
        saveRecentlyViewedList(limitedList)
    }

    /**
     * Get the list of recently viewed products
     */
    fun getRecentlyViewedProducts(): List<ProductDto> {
        val json = prefs.getString(KEY_RECENTLY_VIEWED, null) ?: return emptyList()

        val type: Type = object : TypeToken<List<ProductDto>>() {}.type
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Clear the recently viewed products list
     */
    fun clearRecentlyViewed() {
        prefs.edit().remove(KEY_RECENTLY_VIEWED).apply()
    }

    private fun saveRecentlyViewedList(products: List<ProductDto>) {
        val json = gson.toJson(products)
        prefs.edit().putString(KEY_RECENTLY_VIEWED, json).apply()
    }

    companion object {
        private const val PREFS_NAME = "roomify_prefs"
        private const val KEY_RECENTLY_VIEWED = "recently_viewed_products"
        private const val MAX_ITEMS = 10 // Maximum number of items to keep in recently viewed
    }
}