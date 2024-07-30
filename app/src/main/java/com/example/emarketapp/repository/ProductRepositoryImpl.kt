package com.example.emarketapp.repository

import com.example.emarketapp.data.local.LocalDataSource
import com.example.emarketapp.data.mapper.toProductEntity
import com.example.emarketapp.data.mapper.toProductEntityList
import com.example.emarketapp.data.mapper.toProductUIList
import com.example.emarketapp.data.mapper.toProductUIListFromResponse
import com.example.emarketapp.data.mapper.toProductUIModel
import com.example.emarketapp.data.remote.RemoteDataSource
import com.example.emarketapp.model.ProductListUIModel
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
    override fun getProductList(): Flow<Resource<List<ProductListUIModel>>> = flow {
        emit(Resource.Loading)

        val productList = localDataSource.getProductListFromDB()

        if (productList.isNotEmpty()) {
            emit(Resource.Success(productList.toProductUIList()))
            return@flow
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

    override fun getProduct(id: String): Flow<Resource<ProductListUIModel>> = flow {
        emit(Resource.Loading)
        try {
            val result = localDataSource.getProduct(id)
            emit(Resource.Success(result.toProductUIModel()))
        } catch (throwable: Throwable) {
            emit(Resource.Failure(throwable.message ?: throwable.localizedMessage))
        }
    }

    override fun getSearchedProductFromDB(searchPattern: String): Flow<Resource<List<ProductListUIModel>>> =
        flow {
            emit(Resource.Loading)
            try {
                val result = localDataSource.getSearchedProductFromDB(searchPattern)
                emit(Resource.Success(result.toProductUIList()))
            } catch (throwable: Throwable) {
                emit(Resource.Failure(throwable.message ?: throwable.localizedMessage))
            }
        }


    override fun getProductsBetweenRange(
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

    override fun updateProduct(product: ProductListUIModel) = localDataSource.updateProduct(product.toProductEntity())

    override fun getFavProductList(): Flow<Resource<List<ProductListUIModel>>> = flow {
        emit(Resource.Loading)
        try {
            val result = localDataSource.getFavProductList()
            emit(Resource.Success(result.toProductUIList()))
        } catch (throwable: Throwable) {
            emit(Resource.Failure(throwable.message ?: throwable.localizedMessage))
        }
    }

    override fun getBasketProductList(): Flow<Resource<List<ProductListUIModel>>> = flow {
        emit(Resource.Loading)
        try {
            val result = localDataSource.getBasketProductList()
            emit(Resource.Success(result.toProductUIList()))
        } catch (throwable: Throwable) {
            emit(Resource.Failure(throwable.message ?: throwable.localizedMessage))
        }
    }

}