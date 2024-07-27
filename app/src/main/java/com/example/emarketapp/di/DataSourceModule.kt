package com.example.emarketapp.di

import com.example.emarketapp.data.remote.ProductAPI
import com.example.emarketapp.data.remote.RemoteDataSource
import com.example.emarketapp.data.remote.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideRemoteDataSource(
        productAPI: ProductAPI,
    ): RemoteDataSource =
        RemoteDataSourceImpl(productAPI)
}