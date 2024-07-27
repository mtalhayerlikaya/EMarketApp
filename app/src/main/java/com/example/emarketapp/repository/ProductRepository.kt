package com.example.emarketapp.repository

import com.example.emarketapp.model.ProductModel
import com.example.emarketapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductList(): Flow<Resource<List<ProductModel>>>
    suspend fun getProduct(id: String): ProductModel
}