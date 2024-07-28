package com.example.emarketapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val image: String,
    val price: Double,
    val description: String,
    val model: String,
    val brand: String,
)
