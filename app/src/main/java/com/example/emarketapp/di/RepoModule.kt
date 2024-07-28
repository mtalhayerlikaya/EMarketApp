package com.example.emarketapp.di

import com.example.emarketapp.data.local.LocalDataSource
import com.example.emarketapp.data.remote.RemoteDataSource
import com.example.emarketapp.repository.ProductRepository
import com.example.emarketapp.repository.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    @Singleton
    fun provideProductRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
    ): ProductRepository =
        ProductRepositoryImpl(remoteDataSource, localDataSource)
}