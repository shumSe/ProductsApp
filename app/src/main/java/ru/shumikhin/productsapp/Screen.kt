package ru.shumikhin.productsapp

const val DETAILS_ARGUMENT_KEY = "id"
const val HOME_ARGUMENT_KEY = "type"
const val HOME_SEARCH_ARGUMENT_KEY = "searchArgument"
sealed class Screen(val route: String) {
    data object Home: Screen(route = "home_screen/{$HOME_ARGUMENT_KEY}?search={$HOME_SEARCH_ARGUMENT_KEY}"){
        fun passTypeAndParameter(
            type: Int,
            parameter: String
        ): String{
            return "home_screen/$type?search=$parameter"
        }
    }
    data object Detail: Screen(route = "detail_screen/{$DETAILS_ARGUMENT_KEY}")
    data object Search: Screen(route = "search_screen")
}