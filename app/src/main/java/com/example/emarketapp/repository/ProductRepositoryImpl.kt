package com.example.emarketapp.repository

import com.example.emarketapp.data.local.LocalDataSource
import com.example.emarketapp.data.mapper.toProductEntityList
import com.example.emarketapp.data.mapper.toProductUIList
import com.example.emarketapp.data.mapper.toProductUIListFromResponse
import com.example.emarketapp.data.remote.RemoteDataSource
import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.model.ProductResponse
import com.example.emarketapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl
@Inject
constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : ProductRepository {
    override suspend fun getProductList(): Flow<Resource<List<ProductListUIModel>>> = flow {
        emit(Resource.Loading)

        val productList = localDataSource.getProductListFromDB()

        if (productList.isNotEmpty()) {
            emit(Resource.Success(productList.toProductUIList()))
        }

        val response = try {
            remoteDataSource.getProductListFromAPI()
        } catch (throwable: Throwable) {
            emit(Resource.Failure(throwable.message ?: throwable.localizedMessage))
            null
        }

        response?.let {
            localDataSource.insertProductList(response.toProductEntityList())
            emit(Resource.Success(response.toProductUIListFromResponse()))
        }
    }

    override suspend fun getProduct(id: String): ProductResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getSearchedProductFromDB(searchPattern: String): Flow<Resource<List<ProductListUIModel>>> =
        flow {
            emit(Resource.Loading)
            try {
                val result = localDataSource.getSearchedProductFromDB(searchPattern)
                emit(Resource.Success(result.toProductUIList()))
            } catch (throwable: Throwable) {
                emit(Resource.Failure(throwable.message ?: throwable.localizedMessage))
            }
        }

    override suspend fun getProductsBetweenRange(
        minPrice: Double,
        maxPrice: Double,
    ): Flow<Resource<List<ProductListUIModel>>> = flow {
        emit(Resource.Loading)
        try {
            val result = localDataSource.getProductsBetweenRange(minPrice, maxPrice)
            emit(Resource.Success(result.toProductUIList()))
        } catch (throwable: Throwable) {
            emit(Resource.Failure(throwable.message ?: throwable.localizedMessage))
        }
    }

}