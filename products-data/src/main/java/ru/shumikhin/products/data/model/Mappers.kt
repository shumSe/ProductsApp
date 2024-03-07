package ru.shumikhin.products.data.model

import ru.shumikhin.productsapi.models.ProductDTO

internal fun ProductDTO.toProduct(): Product {
    return Product(
        id = this.id,
        title = this.title,
        description = this.description,
        price = this.price,
        discountPercentage = this.discountPercentage,
        rating = this.rating,
        stock=this.stock,
        brand=this.brand,
        category = this.category,
        thumbnail = this.thumbnail,
        images = this.images
    )
}