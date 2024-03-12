package ru.shumikhin.productsapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.shumikhin.products.details.ProductDetails
import ru.shumikhin.products.details.utils.DETAILS_SCREEN_ARGUMENT_ID
import ru.shumikhin.products.main.ProductsMain
import ru.shumikhin.products.main.utils.ALL_PRODUCTS
import ru.shumikhin.products.main.utils.CATEGORY_PRODUCTS
import ru.shumikhin.products.main.utils.HOME_SCREEN_ARGUMENT_SEARCH
import ru.shumikhin.products.main.utils.HOME_SCREEN_ARGUMENT_TYPE
import ru.shumikhin.products.main.utils.SEARCH_PRODUCTS
import ru.shumikhin.products.search.ProductSearch
import ru.shumikhin.products.search.utils.SEARCH_SCREEN_ARGUMENT_PARAMETER

@Composable
fun SetUpMainNavHost(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route,
            arguments = listOf(navArgument(name = HOME_SCREEN_ARGUMENT_TYPE) {
                defaultValue = ALL_PRODUCTS
                type = NavType.IntType
            },
                navArgument(name = HOME_SCREEN_ARGUMENT_SEARCH) {
                    defaultValue = ""
                    type = NavType.StringType
                }
            )) {
            ProductsMain(
                onItemClick = { productId ->
                    navController.navigate(
                        Screen.Detail.route.replace(
                            oldValue = "{$DETAILS_SCREEN_ARGUMENT_ID}",
                            newValue = productId.toString()
                        )
                    )
                },
                onSearchClick = {searchValue ->
                    navController.navigate(Screen.Search.route.replace(
                        oldValue = "{$SEARCH_SCREEN_ARGUMENT_PARAMETER}",
                        newValue = searchValue
                    ))
                }
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument(name = DETAILS_SCREEN_ARGUMENT_ID) {
                type = NavType.IntType
            })
        ) {
            ProductDetails()
        }

        composable(route = Screen.Search.route, arguments = listOf(
            navArgument(name= SEARCH_SCREEN_ARGUMENT_PARAMETER){
                defaultValue = ""
                type = NavType.StringType
            },
        )) {
            ProductSearch(
                onCategoryClick = {
                    navController.navigate(Screen.Home.passTypeAndParameter(type = CATEGORY_PRODUCTS, parameter = it))
                },
                onFindClick = {
                    navController.navigate(Screen.Home.passTypeAndParameter(type = SEARCH_PRODUCTS, parameter = it))
                }
            )
        }
    }
}