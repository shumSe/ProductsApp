package ru.shumikhin.products.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ru.shumikhin.products.core.design.components.ErrorBlock
import ru.shumikhin.products.core.design.components.RatingIcon
import ru.shumikhin.products.core.design.components.ShowLoading
import ru.shumikhin.products.details.model.ProductDetailsUI

@Composable
fun ProductDetails(
    viewModel: ProductDetailsViewModel = hiltViewModel(),
) {
    Column {
        val state by viewModel.state.collectAsState()
        Spacer(modifier = Modifier.size(8.dp))
        when (val currentState = state) {
            State.Default -> {
                ShowLoading(modifier = Modifier.fillMaxSize())
            }

            is State.Error -> {
                ErrorBlock(
                    mainText = "Can't upload product",
                    errorMessage = currentState.message,
                ) {
                    viewModel.retryLoadProduct()
                }
            }

            State.Loading -> ShowLoading(modifier = Modifier.fillMaxSize())
            is State.Success -> ProductDetailsContainer(product = currentState.product)
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
                .fillMaxHeight(fraction = 0.4f)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(color = Color.DarkGray)
        ) { imageIndex ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
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
            text = "${product.price}$",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = product.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = product.description,
            style = MaterialTheme.typography.titleMedium,
            lineHeight = 26.sp,
        )
        Spacer(modifier = Modifier.size(8.dp))
        RatingIcon(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            rating = product.rating,
            textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            iconSize = 24.dp,
        )
        Spacer(modifier = Modifier.size(10.dp))
    }
}

