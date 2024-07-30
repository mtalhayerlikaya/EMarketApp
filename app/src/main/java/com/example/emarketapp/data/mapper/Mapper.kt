package com.example.emarketapp.data.mapper

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
        basketItemCount = it.basketItemCount
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
        basketItemCount = 0
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
        basketItemCount = 0
    )
}

fun ProductListUIModel.toProductEntity() =

    ProductEntity(
        name = this.name,
        image = this.image,
        price = this.price.toDouble(),
        description = this.description,
        model = this.model,
        brand = this.brand,
        id = this.id,
        isFavorite = this.isFavorite,
        basketItemCount = this.basketItemCount
    )


