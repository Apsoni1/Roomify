package com.majorproject.roomify.Utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ScalePageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val pageHeight = page.height

        // Apply a scale factor and adjust the alpha value
        val scaleFactor = 0.85f + (1 - Math.abs(position)) * 0.15f
        val offset = pageWidth * (1 - scaleFactor) / 2

        page.translationX = if (position < 0) {
            offset - offset / 2
        } else {
            -offset + offset / 2
        }
        page.scaleX = scaleFactor
        page.scaleY = scaleFactor
        page.alpha = 0.5f + (1 - Math.abs(position)) * 0.5f

        page.setPadding((offset).toInt(), 0, (offset).toInt(), 0)
    }
}
