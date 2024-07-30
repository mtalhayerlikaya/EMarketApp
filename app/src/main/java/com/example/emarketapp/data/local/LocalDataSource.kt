package com.example.emarketapp.data.local

import androidx.paging.PagingSource
import com.example.emarketapp.model.ProductEntity

interface LocalDataSource {
    suspend fun insertProductList(productList: List<ProductEntity>)
    fun getSearchedProductFromDB(searchPattern: String): PagingSource<Int, ProductEntity>
    suspend fun getProductListFromDB(): List<ProductEntity>
    fun getProductsBetweenRange(minPrice: Double, maxPrice: Double): PagingSource<Int, ProductEntity>
    fun updateProduct(product: ProductEntity)
    fun getProduct(id: String): ProductEntity
    suspend fun getFavProductList(): List<ProductEntity>
    suspend fun getBasketProductList(): List<ProductEntity>
    fun getAllProducts(): PagingSource<Int, ProductEntity>
}