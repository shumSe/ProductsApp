package ru.shumikhin.products.main

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import ru.shumikhin.products.core.design.components.ErrorBlock
import ru.shumikhin.products.core.design.components.SearchField
import ru.shumikhin.products.core.design.components.ShowLoading
import ru.shumikhin.products.data.utils.errorToMessage
import ru.shumikhin.products.main.components.ProductItem


@Composable
fun ProductsMain(
    viewModel: ProductsMainViewModel = hiltViewModel(),
    onItemClick: (id: Int) -> Unit,
    onSearchClick: (searchValue: String) -> Unit,
) {
    val products = viewModel.productResponse.collectAsLazyPagingItems()
    val screenType = viewModel.getScreenType()


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
        SearchField(
            text = screenType.searchParameter,
            onClickNavigate = { onSearchClick(screenType.searchParameter) }
        )

        LazyVerticalGrid(
            columns = gridCells,
            modifier = Modifier
                .fillMaxWidth(fraction = gridFraction)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if(screenType.categoryName.isNotEmpty()){
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = "${screenType.categoryName.uppercase()}:",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                    )
                }
            }

            items(products.itemCount) { itemIndex ->
                key(products[itemIndex]!!.id) {
                    ProductItem(product = products[itemIndex]!!, onClick = onItemClick)
                }
            }

            products.apply {
                when {

                    loadState.refresh is LoadState.Loading -> {
                        item(span = { GridItemSpan(maxLineSpan) }) { ShowLoading(modifier = Modifier.fillMaxSize()) }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val error = products.loadState.refresh as LoadState.Error
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            ErrorBlock(
                                mainText = "Can't upload products",
                                errorMessage = error.error.errorToMessage(),
                                retryAction = { retry() })
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item(span = { GridItemSpan(maxCurrentLineSpan) }) { ShowLoading(modifier = Modifier) }
                    }

                    loadState.append.endOfPaginationReached -> {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            if (products.itemCount > 0)
                                EndOfPagination()
                            else {
                                ShowEmptyProducts()
                            }
                        }

                    }

                    loadState.append is LoadState.Error -> {
                        val error = products.loadState.append as LoadState.Error
                        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                            ErrorBlock(
                                mainText = "Can't upload products",
                                errorMessage = error.error.errorToMessage(),
                                retryAction = { retry() })
                        }

                    }
                }
            }
            item {
                Spacer(modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
internal fun ShowEmptyProducts() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "No products",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        )
    }
}

@Composable
internal fun EndOfPagination(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = "End of pagination icon"
        )
        Text(text = "All products shown")
    }
}