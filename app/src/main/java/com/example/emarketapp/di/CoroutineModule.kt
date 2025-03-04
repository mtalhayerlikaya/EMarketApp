package com.example.emarketapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @Provides
    @Singleton
    @Named("IO")
    fun provideContextIO(): CoroutineDispatcher = Dispatchers.IO


    @Provides
    @Singleton
    @Named("Default")
    fun provideContextDefault(): CoroutineDispatcher = Dispatchers.Default
}