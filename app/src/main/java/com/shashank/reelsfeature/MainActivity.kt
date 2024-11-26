package com.shashank.reelsfeature

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shashank.reelsfeature.adapter.VideoFeedAdapter
import com.shashank.reelsfeature.databinding.ActivityMainBinding
import com.shashank.reelsfeature.pool.VideoPlayerPool
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var playerPool: VideoPlayerPool
    private lateinit var videoFeedAdapter: VideoFeedAdapter
    private var mBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        init()
        setContentView(mBinding?.root)
    }

    private fun init() {
        val videoUrls = listOf(
            "http://res.cloudinary.com/dku7qimid/video/upload/v1732438868/Foodie/post/video/1732438864859/673b1a63e9dc0d787fd77c96.mov",
            "http://res.cloudinary.com/dku7qimid/video/upload/v1728629791/user-posts/fp2vkpyzpevyjrbndi55.mp4",
            "http://res.cloudinary.com/dku7qimid/video/upload/v1732441004/Foodie/post/video/1732441001350/673b1a63e9dc0d787fd77c96.mov",
            "http://res.cloudinary.com/dku7qimid/video/upload/v1732440304/Foodie/post/video/1732440301091/673b1a63e9dc0d787fd77c96.mov",
            "http://res.cloudinary.com/dku7qimid/video/upload/v1730814769/Foodie/post/video/1730814741638/6729bb21786c0600290096f5.mov",
            "http://res.cloudinary.com/dku7qimid/video/upload/v1732440847/Foodie/post/video/1732440843991/673b1a63e9dc0d787fd77c96.mov",
            "http://res.cloudinary.com/dku7qimid/video/upload/v1731653619/Foodie/post/video/1731653600887/672c8468786c060029009701.mov"
        )

        videoFeedAdapter = VideoFeedAdapter(videoUrls, playerPool)
        
        mBinding?.rvVideo?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = videoFeedAdapter
        }
        mBinding?.rvVideo?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                for (i in 0 until recyclerView.childCount) {
                    val view = recyclerView.getChildAt(i)
                    val position = recyclerView.getChildAdapterPosition(view)

                    val holder = recyclerView.findViewHolderForAdapterPosition(position) as VideoFeedAdapter.VideoViewHolder
                    val player = holder.player
                    if (isViewInViewPort(view)) {
                        videoFeedAdapter.setCurrentlyPlayingPlayer(player)
                        player?.playWhenReady = true  // Start playing if 80% visible
                    } else {
                        player?.playWhenReady = false  // Pause if less than 80% visible
                    }
                }
            }
        })

        // Ensure the first video plays when the app opens, even before scrolling
        playFirstVisibleVideo()
    }

    // Helper function to check if a view is in the viewport (visible area)
    private fun isViewInViewPort(view: View): Boolean {
        val rect = Rect()
        view.getGlobalVisibleRect(rect)
        // Check if more than 80% of the item is visible
        val viewHeight = view.height
        val visibleHeight = rect.height()
        return visibleHeight >= 0.8 * viewHeight
    }

    // Method to trigger playback for the first visible video
    private fun playFirstVisibleVideo() {
        val firstVisibleView = mBinding?.rvVideo?.getChildAt(0)
        if (firstVisibleView != null) {
            val position = mBinding?.rvVideo?.getChildAdapterPosition(firstVisibleView)
            val holder = mBinding?.rvVideo?.findViewHolderForAdapterPosition(position?:0) as VideoFeedAdapter.VideoViewHolder
            val player = holder.player
            if (isViewInViewPort(firstVisibleView)) {
                videoFeedAdapter.setCurrentlyPlayingPlayer(player)
                player?.playWhenReady = true
            }
        }
    }
}