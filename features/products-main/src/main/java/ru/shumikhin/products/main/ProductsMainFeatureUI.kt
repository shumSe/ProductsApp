package ru.shumikhin.products.main

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ru.shumikhin.products.main.utils.ErrorMessage
import ru.shumikhin.products.main.utils.LoadingNextPageItem
import ru.shumikhin.products.main.utils.PageLoader

@Composable
fun ProductsMain() {
    ProductsMain(viewModel = viewModel())
}

@Composable
internal fun ProductsMain(viewModel: ProductsMainViewModel) {
    val response = viewModel.productResponse.collectAsLazyPagingItems()
    ProductsContainer(products = response)
}

@Composable
fun LoadingProducts() {
    Text(text = "Loading")
}

@Composable
fun ProductsEmpty() {
}

@Composable
private fun ProductsContainer(
    products: LazyPagingItems<ProductUI>
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
    ) {
        SearchField()
        LazyVerticalGrid(
            columns = GridCells.Adaptive(180.dp),
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(products.itemCount) { itemIndex ->
                key(products[itemIndex]!!.id) {
                    ProductItem(product = products[itemIndex]!!, onClick = {println(products[itemIndex]!!.id)})
                }
            }
            products.apply {
                when {

                    loadState.refresh is LoadState.Loading -> {
                        item { PageLoader(modifier = Modifier.fillMaxSize()) }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val error = products.loadState.refresh as LoadState.Error
                        item {
                            ErrorMessage(
                                modifier = Modifier.fillMaxWidth(),
                                message = error.error.localizedMessage!!,
                                onClickRetry = { retry() })
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item { LoadingNextPageItem(modifier = Modifier) }
                    }

                    loadState.append.endOfPaginationReached -> {
                        item{ErrorMessage(message = "end of pag", onClickRetry = {})}
                    }

                    loadState.append is LoadState.Error -> {
                        val error = products.loadState.append as LoadState.Error
                        item {
                            ErrorMessage(
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


@Composable
private fun SearchField(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Transparent)
    ) {
        var text by remember { mutableStateOf("") }
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .padding(vertical = 5.dp)
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(10.dp),
            placeholder = {
                Text(
                    text = "Поиск",
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.Gray.copy(alpha = 0.7f))
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Category Icon"
                )
            },
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
    }
}

@Composable
private fun ProductItem(modifier: Modifier = Modifier,
                        product: ProductUI,
                        onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .widthIn(max = 200.dp)
            .heightIn(max = 250.dp)
            .clip(
                shape = RoundedCornerShape(10.dp)
            )
            .background(Color.White)
            .wrapContentSize()
            .clickable(enabled = true, onClick = onClick),
    ) {
        ImageHolder(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxSize()
                .clip(
                    shape = RoundedCornerShape(
                        topEnd = 10.dp,
                        topStart = 10.dp
                    )
                ), imgUrl = product.thumbnail
        )
        Column(
            modifier = Modifier
                .weight(0.4f)
                .padding(10.dp, top = 5.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = product.title, modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                maxLines = 1,
                lineHeight = 16.sp,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleMedium.copy(color = Color.Blue.copy(alpha = 0.7f))
            )
            Text(
                text = "${product.price}$",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            )
            Text(
                text = product.description, modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                maxLines = 2,
                lineHeight = 16.sp,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)
            )
            RatingIcon(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                rating = product.rating
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageHolder(modifier: Modifier, imgUrl: String) {
    Box(modifier = modifier.background(Color.DarkGray)) {
        GlideImage(
            model = imgUrl,
            contentDescription = "Product Img",
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
private fun RatingIcon(modifier: Modifier, rating: Float = 1.6f) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = rating.toString(),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Icon(
            Icons.Outlined.Star,
            contentDescription = "Rating Icon",
            tint = Color.Yellow.copy(green = 0.9f),
            modifier = Modifier
                .size(24.dp)
        )
    }
}

@Preview
@Composable
fun ProductItemPreview(
    @PreviewParameter(
        ProductPreviewProvider::class,
        limit = 1
    ) product: ProductUI
) {
    ProductItem(modifier = Modifier, product = product)
}

private class ProductPreviewProvider : PreviewParameterProvider<ProductUI> {
    override val values: Sequence<ProductUI> = sequenceOf(
        ProductUI(
            id = 1,
            title = "Title",
            description = "Description",
            price = 580,
            rating = 4.7f,
            thumbnail = "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"
        ),
        ProductUI(
            id = 2,
            title = "Its a large title with many letters in it so you can check",
            description = "Description",
            price = 580,
            rating = 4.7f,
            thumbnail = "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"
        ),
        ProductUI(
            id = 3,
            title = "Title",
            description = "Its a large description with many letters in it so you can check how ui react to this many letters and pipipipfghdfmk upiyupity and ptrereyterner",
            price = 580,
            rating = 4.7f,
            thumbnail = "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"
        ),
        ProductUI(
            id = 4,
            title = "Its a large title with many letters in it so you can check",
            description = "Its a large description with many letters in it so you can check how ui react to this many letters and pipipipfghdfmk upiyupity and ptrereyterner",
            price = 580,
            rating = 4.7f,
            thumbnail = "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"
        ),
        ProductUI(
            id = 5,
            title = "Title",
            description = "Description",
            price = 580,
            rating = 4.7f,
            thumbnail = "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"
        ),
        ProductUI(
            id = 6,
            title = "Its a large title with many letters in it so you can check",
            description = "Description",
            price = 580,
            rating = 4.7f,
            thumbnail = "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"
        ),
        ProductUI(
            id = 7,
            title = "Title",
            description = "Its a large description with many letters in it so you can check how ui react to this many letters and pipipipfghdfmk upiyupity and ptrereyterner",
            price = 580,
            rating = 4.7f,
            thumbnail = "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"
        ),
        ProductUI(
            id = 8,
            title = "Its a large title with many letters in it so you can check",
            description = "Its a large description with many letters in it so you can check how ui react to this many letters and pipipipfghdfmk upiyupity and ptrereyterner",
            price = 580,
            rating = 4.7f,
            thumbnail = "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"
        )
    )

}

private class ProductsListPreviewProvider : PreviewParameterProvider<List<ProductUI>> {
    private val productProvider = ProductPreviewProvider()
    override val values: Sequence<List<ProductUI>> = sequenceOf(
        productProvider.values.toList()
    )

}


