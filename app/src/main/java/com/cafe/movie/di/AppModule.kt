package com.cafe.movie.di

import android.content.Context
import com.cafe.movie.CafeMovieApplication
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): CafeMovieApplication {
        return app as CafeMovieApplication
    }

    @Singleton
    @Provides
    fun provideGson() = Gson()

}