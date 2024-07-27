package com.example.emarketapp.data.remote

import com.example.emarketapp.model.ProductModel
import javax.inject.Inject


class RemoteDataSourceImpl
@Inject
constructor(private val productAPI: ProductAPI) : RemoteDataSource {
    override suspend fun getProductListFromAPI(): List<ProductModel> = productAPI.getProductListFromAPI()
    override suspend fun getProductFromAPI(id: String): ProductModel = productAPI.getProductByID(id)
}

