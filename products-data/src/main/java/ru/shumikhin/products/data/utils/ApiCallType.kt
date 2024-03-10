package ru.shumikhin.products.data.utils

sealed class ApiCallType {
    data object Default: ApiCallType()
    data class Search(val searchParameter: String): ApiCallType()
    data class Category(val categoryName: String): ApiCallType()

}