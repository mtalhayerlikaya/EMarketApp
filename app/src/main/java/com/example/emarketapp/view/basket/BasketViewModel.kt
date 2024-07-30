package com.example.emarketapp.view.basket

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
class BasketViewModel
@Inject constructor(private val productRepository: ProductRepository) : ViewModel() {

    private val _basketProducts = MutableStateFlow<Resource<List<ProductListUIModel>>>(Resource.Loading)
    val basketProducts: StateFlow<Resource<List<ProductListUIModel>>> = _basketProducts


    fun getBasketProductList() = viewModelScope.launch {
        productRepository.getBasketProductList().collect {
            _basketProducts.emit(it)
        }
    }

    fun updateProduct(product: ProductListUIModel) {
        productRepository.updateProduct(product)
    }

    fun getTotalPrice(product: ProductListUIModel) {
        productRepository.updateProduct(product)
    }

}