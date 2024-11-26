package com.shashank.reelsfeature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.shashank.reelsfeature.R
import com.shashank.reelsfeature.databinding.ItemSingleVideoBinding
import com.shashank.reelsfeature.pool.VideoPlayerPool
class VideoFeedAdapter(
    private val videoUrls: List<String>,
    private val playerPool: VideoPlayerPool
) : RecyclerView.Adapter<VideoFeedAdapter.VideoViewHolder>() {

    private var currentlyPlayingPlayer: ExoPlayer? = null

    inner class VideoViewHolder(private val binding: ItemSingleVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        var player: ExoPlayer? = null

        @OptIn(UnstableApi::class)
        fun bind(videoUrl: String) {
            if (player == null) {
                player = playerPool.getPlayer()
                val mediaItem = MediaItem.fromUri(videoUrl)
                player?.setMediaItem(mediaItem)
                player?.prepare()
                binding.playerView.player = player
            }
        }

        fun releasePlayer() {
            player?.release()
            player = null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemSingleVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoUrl = videoUrls[position]
        holder.bind(videoUrl)
    }

    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)
        holder.releasePlayer()  // Release the player when the item is recycled
    }

    override fun getItemCount(): Int = videoUrls.size

    // Method to control which player is currently playing
    fun setCurrentlyPlayingPlayer(player: ExoPlayer?) {
        currentlyPlayingPlayer?.playWhenReady = false
        currentlyPlayingPlayer = player
    }
}
