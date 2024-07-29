package com.example.emarketapp.data.local

import com.example.emarketapp.model.ProductEntity
import javax.inject.Inject

class LocalDataSourceImpl
@Inject
constructor(private val productDAO: ProductDAO) : LocalDataSource {
    override suspend fun insertProductList(productList: List<ProductEntity>) =
        productDAO.insertAllProductsToDB(productList)

    override suspend fun getSearchedProductFromDB(searchPattern: String): List<ProductEntity> =
        productDAO.findProductByName(searchPattern)

    override suspend fun getProductListFromDB(): List<ProductEntity> =
        productDAO.getProductList()

    override suspend fun getProductsBetweenRange(
        minPrice: Double,
        maxPrice: Double,
    ): List<ProductEntity> =
        productDAO.findProductBetweenRange(minPrice, maxPrice)

    override fun setInBasket(productID: String, inBasket: Boolean) =
        productDAO.setInBasket(productID, inBasket)

    override fun getProduct(id: String): ProductEntity = productDAO.getProduct(id)
}