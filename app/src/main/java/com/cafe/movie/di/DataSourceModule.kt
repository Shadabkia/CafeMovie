package com.cafe.movie.di

import com.cafe.movie.data.datasource.MovieDataSourceImp
import com.cafe.movie.data.datasource.MovieRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindMovieRemoteDataSource(dataSource: MovieDataSourceImp): MovieRemoteDataSource


}