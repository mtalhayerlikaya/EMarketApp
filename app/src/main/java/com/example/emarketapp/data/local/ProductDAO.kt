package com.example.emarketapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.emarketapp.model.ProductEntity

@Dao
interface ProductDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProductsToDB(products: List<ProductEntity>)

    @Query("SELECT * FROM product")
    suspend fun getProductList(): List<ProductEntity>

    @Query("SELECT * FROM product WHERE name LIKE '%' || :searchPattern || '%' OR name LIKE '%' || :searchPattern || '%'")
    suspend fun findProductByName(searchPattern: String): List<ProductEntity>

    @Query("SELECT * FROM product WHERE price BETWEEN :minPrice AND :maxPrice")
    suspend fun findProductBetweenRange(minPrice: Double, maxPrice: Double): List<ProductEntity>

    @Query("SELECT * FROM product WHERE id = :productID")
    fun getProduct(productID: String): ProductEntity

    @Query("SELECT * FROM product WHERE isFavorite is 1")
    suspend fun getFavProductList(): List<ProductEntity>

    @Query("SELECT * FROM product WHERE basketItemCount is not 0")
    suspend fun getBasketProductList(): List<ProductEntity>
    @Update
    fun updateProduct(product: ProductEntity)
}