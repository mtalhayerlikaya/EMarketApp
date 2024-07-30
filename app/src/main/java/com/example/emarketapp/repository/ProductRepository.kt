package com.example.emarketapp.repository

import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProductList(): Flow<Resource<List<ProductListUIModel>>>
    fun getProduct(id: String): Flow<Resource<ProductListUIModel>>
    fun getSearchedProductFromDB(searchPattern: String): Flow<Resource<List<ProductListUIModel>>>
    fun getProductsBetweenRange(minPrice: Double, maxPrice: Double): Flow<Resource<List<ProductListUIModel>>>
    fun updateProduct(product: ProductListUIModel)
    fun getFavProductList(): Flow<Resource<List<ProductListUIModel>>>
    fun getBasketProductList(): Flow<Resource<List<ProductListUIModel>>>
}