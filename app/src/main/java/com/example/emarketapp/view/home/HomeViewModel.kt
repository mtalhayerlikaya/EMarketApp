package com.example.emarketapp.view.home

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
class HomeViewModel
@Inject constructor(private val productRepository: ProductRepository) : ViewModel() {

    private val _allProducts = MutableStateFlow<Resource<List<ProductListUIModel>>>(Resource.Loading)
    val allProducts: StateFlow<Resource<List<ProductListUIModel>>> = _allProducts


    fun getProductList() = viewModelScope.launch {
        productRepository.getProductList().collect {
            _allProducts.emit(it)
        }
    }

    private val _searchProductFlow = MutableStateFlow<Resource<List<ProductListUIModel>>>(Resource.Loading)
    val searchProductFlow: MutableStateFlow<Resource<List<ProductListUIModel>>> = _searchProductFlow


    fun searchProduct(searchPattern: String) = viewModelScope.launch {
        productRepository.getSearchedProductFromDB(searchPattern).collect {
            _searchProductFlow.emit(it)
        }
    }

}