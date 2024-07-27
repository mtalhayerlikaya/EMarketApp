package com.example.emarketapp.data.remote

import com.example.emarketapp.model.ProductModel
import com.example.emarketapp.utils.Constants.ALL_PRODUCTS
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductAPI {

    @GET(ALL_PRODUCTS)
    suspend fun getProductListFromAPI(): List<ProductModel>


    @GET(ALL_PRODUCTS)
    suspend fun getProductByID(@Path("id") id: String): ProductModel
}