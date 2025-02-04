package com.majorproject.roomify.fragments
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.VideoView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.majorproject.roomify.adapters.ViewPagerAdapterHome
import com.majorproject.roomify.R

class Home : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private val handler = Handler(Looper.getMainLooper())
    private val autoScrollInterval: Long = 3000 // 3 seconds
    private var currentPage = 0
    private var isAutoScrollActive = true
    private lateinit var videoView: VideoView
    private var isVideoViewInView = false
    private lateinit var startShopping: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.discount_sale_viewpager)
        tabLayout = view.findViewById(R.id.tab_layout)
        startShopping= view.findViewById(R.id.Start_button)

//        start shopping button
        startShopping.setOnClickListener {
            val category= Category()

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.mainframeLayout, category)
                .addToBackStack("Home")
                .commit()
        }

//        Custom searchView    //
        val searchView: androidx.appcompat.widget.SearchView =view.findViewById(R.id.search_view)
//        search view custom hint text
        val searchtext= searchView.findViewById<AutoCompleteTextView>(androidx.appcompat.R.id.search_src_text)
        val color = ContextCompat.getColor(requireContext(),R.color.black)
        searchtext.setTextColor(color)
        searchtext.setHintTextColor(Color.GRAY)
        searchtext.textSize = 14f

        // Sample data for ViewPager2
        val images = listOf(R.drawable.offer_poster0,R.drawable.offer_poster6, R.drawable.offer_poster4, R.drawable.offer_poster2)
        val titles = listOf("Amazing Discounts Awaiting!", "Summer Sale!", "Exclusive Offers")
        val subtitles = listOf("Up to 50%, Terms and condition apply", "Save big on summer essentials", "Limited time only!")

        // Setting the adapter for ViewPager2
        viewPager.adapter = ViewPagerAdapterHome(images, titles, subtitles)


        // Connecting TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position -> }.attach()

        // Auto-scroll feature
        startAutoScroll()

        // Sample categories data
        val categories = listOf(
            "Sofa" to R.drawable.sofa_pngimage,
            "Desk" to R.drawable.desk,
            "Bed" to R.drawable.bed,
            "Shelf" to R.drawable.shelf,
            "Table" to R.drawable.table,
            "Chair" to R.drawable.chair,
            "Cupboard" to R.drawable.cupboard,
            "Stool" to R.drawable.sitting
            // Add more categories here
        )

        val categoriesLayout: LinearLayout = view.findViewById(R.id.category_layout)

        for ((categoryName, categoryImageRes) in categories) {
            val categoryView = layoutInflater.inflate(R.layout.category_item, categoriesLayout, false)

            val imageView: ImageView = categoryView.findViewById(R.id.category_image)
            val textView: TextView = categoryView.findViewById(R.id.category_name)

            imageView.setImageResource(categoryImageRes)
            textView.text = categoryName

            // Set touch listener for elevation effect
            categoryView.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> v.elevation = 8f // Increase elevation on press
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.elevation = 4f // Reset elevation
                }
                false
            }

            categoriesLayout.addView(categoryView)
//            videoview
            videoView = view.findViewById<VideoView>(R.id.Video_home)
            videoView.setVideoPath("android.resource://" + "com.majorproject.roomify" + "/" + R.raw.sample_video)

            // Start playing the video automatically
            videoView.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.isLooping = true  // Set looping if desired
            }
//            set video playing on scrolling only
            val scrollview= view.findViewById<ScrollView>(R.id.scrollView)
            scrollview.viewTreeObserver.addOnScrollChangedListener{
                isVideoViewInView = checkVideoViewInView(scrollview)
                if (isVideoViewInView && !videoView.isPlaying) {
                    videoView.start()
                } else if (!isVideoViewInView && videoView.isPlaying) {
                    videoView.pause()
                }
            }
        }
    }

    private fun checkVideoViewInView(scrollView: ScrollView): Boolean {
        val scrollBounds = Rect()
        scrollView.getHitRect(scrollBounds)

        // Check if the VideoView is within the bounds of the ScrollView
        return videoView.getLocalVisibleRect(scrollBounds)
    }

    override fun onResume() {
        super.onResume()
        if (isVideoViewInView()) {
            videoView.start()
        } // Resume playing when the user navigates back to this fragment
    }

    override fun onPause() {
        super.onPause()
        videoView.pause()  // Pause the video if the user navigates away
    }

    private fun startAutoScroll() {
        val runnable = object : Runnable {
            override fun run() {
                if (isAutoScrollActive) {
                    currentPage = (currentPage + 1) % viewPager.adapter!!.itemCount
                    viewPager.setCurrentItem(currentPage, true)
                    handler.postDelayed(this, autoScrollInterval)
                }
            }
        }
        handler.postDelayed(runnable, autoScrollInterval)

        // Stop auto-scroll on user interaction
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {

                    isAutoScrollActive = false
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    isAutoScrollActive = true
                }
            }
        })
    }
    private fun isVideoViewInView(): Boolean {
        val scrollView = view?.findViewById<ScrollView>(R.id.scrollView)
        return scrollView?.let {
            val videoViewTop = videoView.top
            val videoViewBottom = videoView.bottom
            val scrollViewHeight = it.height
            val scrollY = it.scrollY
            scrollY + scrollViewHeight > videoViewTop && scrollY < videoViewBottom
        } ?: false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}
