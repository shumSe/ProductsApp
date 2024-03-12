package ru.shumikhin.products.main.utils

sealed class ScreenType(val searchParameter: String = "", val categoryName: String = "") {
    data object All: ScreenType()
    class Search(sParam: String): ScreenType(searchParameter = sParam)
    class Category(category: String): ScreenType(categoryName =  category)
}

const val ALL_PRODUCTS: Int = 0
const val SEARCH_PRODUCTS: Int = 1
const val CATEGORY_PRODUCTS: Int = 2