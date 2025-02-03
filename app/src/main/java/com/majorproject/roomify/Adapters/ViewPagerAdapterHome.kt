package com.majorproject.roomify.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.majorproject.roomify.R

class ViewPagerAdapterHome(
    private val images: List<Int>,
    private val titles: List<String>,
    private val subtitles: List<String>,

    ) : RecyclerView.Adapter<ViewPagerAdapterHome.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewpager_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.page_image)
        val cardView: CardView = itemView.findViewById(R.id.click_cardview) // Assuming you have a CardView in viewpager_item.xml

        fun bind(imageRes: Int) {
            imageView.setImageResource(imageRes)
        }
    }
}