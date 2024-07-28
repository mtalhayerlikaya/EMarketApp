package com.example.emarketapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.emarketapp.model.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDAO
}