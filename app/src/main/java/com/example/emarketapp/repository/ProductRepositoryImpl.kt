package com.example.emarketapp.repository

import com.example.emarketapp.data.remote.RemoteDataSource
import com.example.emarketapp.model.ProductModel
import com.example.emarketapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl
@Inject
constructor(
    private val remoteDataSource: RemoteDataSource,
    // private val dispatcher: CoroutineDispatcher
) : ProductRepository {
    override suspend fun getProductList(): Flow<Resource<List<ProductModel>>> = flow {
        emit(Resource.Loading)

        val result = try {
            remoteDataSource.getProductListFromAPI()
        } catch (throwable: Throwable) {
            emit(Resource.Failure(throwable.message ?: throwable.localizedMessage))
            null
        }
        emit(Resource.Success(result!!))
        /* if (result.isNullOrEmpty()) {
             val localResult = localCryptoData.getAllCryptosFromDB()
             emit(Resource.Success(CryptoDBEntityMapper().toCryptoList(localResult)))
         } else {
             localCryptoData.insertCryptoList(CryptoDBEntityMapper().toEntityList(result))
             emit(Resource.Success(result))
         }*/

    }

    override suspend fun getProduct(id: String): ProductModel {
        TODO("Not yet implemented")
    }

}