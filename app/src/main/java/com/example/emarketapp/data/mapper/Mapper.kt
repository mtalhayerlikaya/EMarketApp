package com.example.emarketapp.data.mapper

import androidx.lifecycle.LiveData
import com.example.emarketapp.model.ProductEntity
import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.model.ProductResponse

fun List<ProductEntity>.toProductUIList() = map {
    ProductListUIModel(
        name = it.name,
        image = it.image,
        price = it.price.toString(),
        description = it.description,
        model = it.model,
        brand = it.brand,
        id = it.id,
        isFavorite = it.isFavorite,
        isInBasket = it.isInBasket
    )
}

fun List<ProductResponse>.toProductUIListFromResponse() = map {
    ProductListUIModel(
        name = it.name,
        image = it.image,
        price = it.price,
        description = it.description,
        model = it.model,
        brand = it.brand,
        id = it.id,
        isFavorite = false,
        isInBasket = false
    )
}

fun List<ProductResponse>.toProductEntityList() = map {
    ProductEntity(
        name = it.name,
        image = it.image,
        price = it.price.toDouble(),
        description = it.description,
        model = it.model,
        brand = it.brand,
        id = it.id,
        isFavorite = false,
        isInBasket = false
    )
}

fun LiveData<ProductEntity>.toProductUIListModel(): ProductListUIModel? {
    val productEntity = this.value ?: return null

    return ProductListUIModel(
        name = productEntity.name,
        image = productEntity.image,
        price = productEntity.price.toString(), // Convert Double to String
        description = productEntity.description,
        model = productEntity.model,
        brand = productEntity.brand,
        id = productEntity.id,
        isFavorite = productEntity.isFavorite,
        isInBasket = productEntity.isInBasket
    )
}

