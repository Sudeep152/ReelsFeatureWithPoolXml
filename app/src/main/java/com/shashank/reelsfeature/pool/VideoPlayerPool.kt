package com.shashank.reelsfeature.pool

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer

class VideoPlayerPool(context: Context) {
    private val playerPool = mutableListOf<ExoPlayer>()
    private val context = context

    fun getPlayer(): ExoPlayer {
        return if (playerPool.isNotEmpty()) {
            playerPool.removeAt(0)
        } else {
            ExoPlayer.Builder(context).build()
        }
    }

    fun releasePlayer(player: ExoPlayer) {
        player.release()
        playerPool.add(player)
    }

    fun clear() {
        playerPool.forEach { it.release() }
        playerPool.clear()
    }
}
