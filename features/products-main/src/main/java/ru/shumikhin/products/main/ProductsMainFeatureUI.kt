package ru.shumikhin.products.main

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import ru.shumikhin.products.core.design.components.SearchField
import ru.shumikhin.products.core.design.components.ShowError
import ru.shumikhin.products.core.design.components.ShowLoading
import ru.shumikhin.products.main.components.ProductItem
import ru.shumikhin.products.main.utils.LoadingNextPageItem


@Composable
fun ProductsMain(
    viewModel: ProductsMainViewModel = hiltViewModel(),
    onItemClick: (id: Int) -> Unit,
    onSearchClick: () -> Unit,
) {
    val products = viewModel.productResponse.collectAsLazyPagingItems()

    val configuration = LocalConfiguration.current
    val gridCells =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            GridCells.Fixed(4)
        else
            GridCells.Adaptive(180.dp)

    val gridFraction =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            0.9f
        else
            1f

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchField(onClickNavigate = onSearchClick)

            LazyVerticalGrid(
                columns = gridCells,
                modifier = Modifier
                    .fillMaxWidth(fraction = gridFraction)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(products.itemCount) { itemIndex ->
                    key(products[itemIndex]!!.id) {
                        ProductItem(product = products[itemIndex]!!, onClick = onItemClick)
                    }
                }

            products.apply {
                when {

                    loadState.refresh is LoadState.Loading -> {
                        item { ShowLoading(modifier = Modifier.fillMaxSize()) }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val error = products.loadState.refresh as LoadState.Error
                        item {
                            ShowError(
                                modifier = Modifier.fillMaxWidth(),
                                message = error.error.localizedMessage!!,
                                onClickRetry = { retry() })
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item { LoadingNextPageItem(modifier = Modifier) }
                    }

                    loadState.append.endOfPaginationReached -> {
                        item { ShowError(message = "end of pag", onClickRetry = {}) }
                    }

                    loadState.append is LoadState.Error -> {
                        val error = products.loadState.append as LoadState.Error
                        item {
                            ShowError(
                                modifier = Modifier,
                                message = error.error.localizedMessage!!,
                                onClickRetry = { retry() })
                        }

                    }
                }
            }
            item {
                Spacer(modifier = Modifier.size(10.dp))
            }
        }
    }
}