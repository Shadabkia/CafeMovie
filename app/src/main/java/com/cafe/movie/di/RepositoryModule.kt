package com.cafe.movie.di

import com.cafe.movie.data.repository.MovieRepository
import com.cafe.movie.data.repository.MovieRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMovieRepository(repository: MovieRepositoryImp): MovieRepository

}