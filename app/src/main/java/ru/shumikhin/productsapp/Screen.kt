package ru.shumikhin.productsapp

import ru.shumikhin.products.details.utils.DETAILS_SCREEN_ARGUMENT_ID
import ru.shumikhin.products.main.utils.HOME_SCREEN_ARGUMENT_SEARCH
import ru.shumikhin.products.main.utils.HOME_SCREEN_ARGUMENT_TYPE
import ru.shumikhin.products.search.utils.SEARCH_SCREEN_ARGUMENT_PARAMETER

sealed class Screen(val route: String) {
    data object Home: Screen(route = "home_screen/{$HOME_SCREEN_ARGUMENT_TYPE}?search={$HOME_SCREEN_ARGUMENT_SEARCH}"){
        fun passTypeAndParameter(
            type: Int,
            parameter: String
        ): String{
            return "home_screen/$type?search=$parameter"
        }
    }
    data object Detail: Screen(route = "detail_screen/{$DETAILS_SCREEN_ARGUMENT_ID}")
    data object Search: Screen(route = "search_screen?search={$SEARCH_SCREEN_ARGUMENT_PARAMETER}")
}