package ru.shumikhin.products.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun ProductDetails(
    viewModel: ProductDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
) {
    Column {
        val state by viewModel.state.collectAsState()
        SearchField()
        Spacer(modifier = Modifier.size(8.dp))
        when (val currentState = state) {
            State.Default -> {
                Loading()
            }

            State.Error -> Error(
                onRetry = { viewModel.retryLoad() }
            )

            State.Loading -> Loading()
            is State.Success -> ProductDetailsContainer(product = currentState.product)
        }
    }
}

@Composable
private fun Loading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Error(
    onRetry: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Error", style = MaterialTheme.typography.titleLarge, color = Color.Red)
            Button(onClick = onRetry) {
                Text(text = "Retry")
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
private fun ProductDetailsContainer(
    product: ProductDetailsUI
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .verticalScroll(
                rememberScrollState()
            ),
    ) {
        val pagerState = rememberPagerState(pageCount = {
            product.images.size
        })
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.5f)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(color = Color.DarkGray)
        ) { imageIndex ->
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                GlideImage(
                    model = product.images.reversed()[imageIndex],
                    contentDescription = "Product image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .widthIn(200.dp, 400.dp)
                        .heightIn(300.dp, 400.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = product.title,
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Blue.copy(alpha = 0.7f)
        )
        Text(
            text = "${product.price}$",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        )
        Text(
            text = product.description,
            style = MaterialTheme.typography.headlineSmall,
            lineHeight = 26.sp,
        )
        Spacer(modifier = Modifier.size(10.dp))
        RatingIcon(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            rating = product.rating
        )
        Spacer(modifier = Modifier.size(10.dp))
    }
}

@Composable
private fun RatingIcon(modifier: Modifier, rating: Float = 1.6f) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = rating.toString(),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Icon(
            Icons.Outlined.Star,
            contentDescription = "Rating Icon",
            tint = Color.Yellow.copy(green = 0.9f),
            modifier = Modifier
                .fillMaxHeight()
        )
    }
}

@Composable
private fun SearchField(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        var text by remember { mutableStateOf("") }
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Icon Back")
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(10.dp),
            placeholder = {
                Text(
                    text = "Поиск",
                    style = LocalTextStyle.current.copy(color = Color.Gray.copy(alpha = 0.7f))
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
            ),
            singleLine = true,
        )
    }
}