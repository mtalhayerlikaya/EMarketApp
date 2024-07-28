package com.example.emarketapp.data.remote

import com.example.emarketapp.model.ProductResponse

interface RemoteDataSource {
    suspend fun getProductListFromAPI(): List<ProductResponse>
    suspend fun getProductFromAPI(id: String): ProductResponse
}