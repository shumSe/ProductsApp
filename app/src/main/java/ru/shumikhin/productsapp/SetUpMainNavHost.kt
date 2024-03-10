package ru.shumikhin.productsapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.shumikhin.products.details.ProductDetails
import ru.shumikhin.products.main.ProductsMain
import ru.shumikhin.products.search.ProductSearch

@Composable
fun SetUpMainNavHost(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route,
            arguments = listOf(navArgument(name = HOME_ARGUMENT_KEY) {
                defaultValue = 0
                type = NavType.IntType
            },
                navArgument(name = HOME_SEARCH_ARGUMENT_KEY) {
                    defaultValue = ""
                    type = NavType.StringType
                }
            )) {
            ProductsMain(
                onItemClick = { productId ->
                    navController.navigate(
                        Screen.Detail.route.replace(
                            oldValue = "{$DETAILS_ARGUMENT_KEY}",
                            newValue = productId.toString()
                        )
                    )
                },
                onSearchClick = {searchValue ->
                    navController.navigate(Screen.Search.route.replace(
                        oldValue = "{$SEARCH_ARGUMENT_KEY}",
                        newValue = searchValue
                    ))
                }
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument(name = DETAILS_ARGUMENT_KEY) {
                type = NavType.IntType
            })
        ) {
            ProductDetails()
        }

        composable(route = Screen.Search.route, arguments = listOf(
            navArgument(name= SEARCH_ARGUMENT_KEY){
                defaultValue = ""
                type = NavType.StringType
            }
        )) {
            ProductSearch(
                onCategoryClick = {
                    navController.navigate(Screen.Home.passTypeAndParameter(type = 2, parameter = it))
                },
                onFindClick = {
                    navController.navigate(Screen.Home.passTypeAndParameter(type = 1, parameter = it))
                }
            )
        }
    }
}