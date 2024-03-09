package ru.shumikhin.productsapp

const val DETAILS_ARGUMENT_KEY = "id"
sealed class Screen(val route: String) {
    data object Home: Screen(route = "home_screen")
    data object Detail: Screen(route = "detail_screen/{$DETAILS_ARGUMENT_KEY}")

    data object Search: Screen(route = "search_screen ")
    data object Category: Screen(route = "category_screen ")
}