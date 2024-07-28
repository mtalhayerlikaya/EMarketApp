package com.example.emarketapp.repository

import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.model.ProductResponse
import com.example.emarketapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductList(): Flow<Resource<List<ProductListUIModel>>>
    suspend fun getProduct(id: String): ProductResponse
    suspend fun getSearchedProductFromDB(searchPattern: String): Flow<Resource<List<ProductListUIModel>>>
    suspend fun getProductsBetweenRange(minPrice: Double, maxPrice: Double): Flow<Resource<List<ProductListUIModel>>>
}