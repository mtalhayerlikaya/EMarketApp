package com.example.emarketapp.data.remote

import com.example.emarketapp.model.ProductResponse
import javax.inject.Inject


class RemoteDataSourceImpl
@Inject
constructor(private val productAPI: ProductAPI) : RemoteDataSource {
    override suspend fun getProductListFromAPI(): List<ProductResponse> = productAPI.getProductListFromAPI()
    override suspend fun getProductFromAPI(id: String): ProductResponse = productAPI.getProductByID(id)
}

