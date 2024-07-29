package com.example.emarketapp.di

import android.content.Context
import androidx.room.Room
import com.example.emarketapp.data.local.ProductDAO
import com.example.emarketapp.data.local.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext appContext: Context): ProductDatabase =
        Room.databaseBuilder(
            appContext,
            ProductDatabase::class.java,
            "main.db"
        ).allowMainThreadQueries().build()

    @Provides
    @Singleton
    fun provideProductDAO(productDatabase: ProductDatabase): ProductDAO =
        productDatabase.productDao()
}