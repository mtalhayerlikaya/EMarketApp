package com.example.emarketapp.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.repository.ProductRepository
import com.example.emarketapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel
@Inject constructor(private val productRepository: ProductRepository) : ViewModel() {

    private val _productDetail = MutableStateFlow<Resource<ProductListUIModel>>(Resource.Loading)
    val productDetail: StateFlow<Resource<ProductListUIModel>> = _productDetail


    fun getProductDetail(productID: String) = viewModelScope.launch {
        productRepository.getProduct(productID).collect {
            _productDetail.emit(it)
        }
    }

    fun updateProduct(product: ProductListUIModel) {
        productRepository.updateProduct(product)
    }

}