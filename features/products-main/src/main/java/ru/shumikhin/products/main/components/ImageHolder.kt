package ru.shumikhin.products.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun ImageHolder(modifier: Modifier, imgUrl: String) {
    Box(modifier = modifier.background(Color.DarkGray)) {
        GlideImage(
            model = imgUrl,
            contentDescription = "Product Img",
            contentScale = ContentScale.FillBounds
        )
    }
}