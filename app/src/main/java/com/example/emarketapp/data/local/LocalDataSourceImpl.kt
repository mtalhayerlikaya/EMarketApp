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

    override fun updateProduct(product: ProductEntity) = productDAO.updateProduct(product)

    override fun getProduct(id: String): ProductEntity = productDAO.getProduct(id)

    override suspend fun getFavProductList(): List<ProductEntity> = productDAO.getFavProductList()
    override suspend fun getBasketProductList(): List<ProductEntity> = productDAO.getBasketProductList()
    override fun updateProductListAfterPurhasing(productList: List<ProductEntity>) =
        productDAO.updateProductList(productList)
}