package com.example.emarketapp.view.home

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.emarketapp.model.ProductEntity
import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class HomeViewModel
@Inject constructor(private val productRepository: ProductRepository) : ViewModel() {
    val products: Flow<PagingData<ProductEntity>> = productRepository.getAllProducts()

    fun searchProduct(searchPattern: String) = productRepository.getSearchedProductFromDB(searchPattern)

    fun getProductsBetweenRange(minPrice: Double, maxPrice: Double) =
        productRepository.getProductsBetweenRange(minPrice, maxPrice)

    fun updateProduct(product: ProductListUIModel) {
        productRepository.updateProduct(product)
    }
}