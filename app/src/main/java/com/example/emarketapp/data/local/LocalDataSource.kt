package com.example.emarketapp.data.local

import com.example.emarketapp.model.ProductEntity

interface LocalDataSource {
    suspend fun insertProductList(productList: List<ProductEntity>)
    suspend fun getSearchedProductFromDB(searchPattern: String): List<ProductEntity>
    suspend fun getProductListFromDB(): List<ProductEntity>
    suspend fun getProductsBetweenRange(minPrice: Double, maxPrice: Double): List<ProductEntity>
    fun updateProduct(product: ProductEntity)
    fun getProduct(id: String): ProductEntity
}