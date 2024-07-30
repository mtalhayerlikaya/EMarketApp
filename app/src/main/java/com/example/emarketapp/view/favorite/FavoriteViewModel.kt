package com.example.emarketapp.view.favorite

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
class FavoriteViewModel
@Inject constructor(private val productRepository: ProductRepository) : ViewModel() {

    private val _favProducts = MutableStateFlow<Resource<List<ProductListUIModel>>>(Resource.Loading)
    val favProducts: StateFlow<Resource<List<ProductListUIModel>>> = _favProducts


    fun getFavProductList() = viewModelScope.launch {
        productRepository.getFavProductList().collect {
            _favProducts.emit(it)
        }
    }

    fun updateProduct(product: ProductListUIModel) {
        productRepository.updateProduct(product)
    }

}