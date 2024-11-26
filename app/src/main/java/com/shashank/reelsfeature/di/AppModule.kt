package com.shashank.reelsfeature.di

import android.content.Context
import com.shashank.reelsfeature.pool.VideoPlayerPool
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context.applicationContext  // Use the application context here
    }

    @Provides
    @Singleton
    fun provideVideoPlayerPool(context: Context): VideoPlayerPool {
        return VideoPlayerPool(context)
    }

}