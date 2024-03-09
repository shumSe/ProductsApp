package ru.shumikhin.productsapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.shumikhin.products.details.ProductDetails
import ru.shumikhin.products.main.ProductsMain

@Composable
fun SetUpMainNavHost(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = Screen.Home.route){
        composable(route=Screen.Home.route){
            ProductsMain(
                onItemClick = {productId ->
                    navController.navigate(Screen.Detail.route.replace(oldValue = "{$DETAILS_ARGUMENT_KEY}", newValue= productId.toString()))
                }
            )
        }
        composable(route = Screen.Detail.route, arguments = listOf(navArgument(name = DETAILS_ARGUMENT_KEY){
            type = NavType.IntType
        })){
            ProductDetails()
        }

        composable(route=Screen.Search.route){

        }
    }
}