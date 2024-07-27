package com.example.emarketapp.data.remote

import com.example.emarketapp.model.ProductModel

interface RemoteDataSource {
    suspend fun getProductListFromAPI(): List<ProductModel>
    suspend fun getProductFromAPI(id: String): ProductModel
}