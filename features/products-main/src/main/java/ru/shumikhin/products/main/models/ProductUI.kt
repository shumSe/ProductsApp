package ru.shumikhin.products.main.models

data class ProductUI(
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val rating: Float,
    val thumbnail: String,
)

