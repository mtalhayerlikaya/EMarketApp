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
        id = it.id
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
        id = it.id
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
        id = it.id
    )
}



