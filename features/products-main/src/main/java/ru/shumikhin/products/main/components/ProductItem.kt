package ru.shumikhin.products.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.shumikhin.products.core.design.components.RatingIcon
import ru.shumikhin.products.main.models.ProductUI


@Composable
internal fun ProductItem(
    modifier: Modifier = Modifier,
    product: ProductUI,
    onClick: (id: Int) -> Unit = {},
) {
    Column(
        modifier = modifier
            .widthIn(max = 220.dp)
            .heightIn(min = 260.dp, max = 260.dp)
            .clip(
                shape = RoundedCornerShape(10.dp)
            )
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .wrapContentSize()
            .clickable(enabled = true, onClick = {
                onClick(product.id)
            }),
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
                .padding(10.dp, top = 5.dp)
            ,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                maxLines = 1,
                lineHeight = 16.sp,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.size(3.dp))
            Text(
                text = "${product.price}$",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = product.description, modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                maxLines = 3,
                lineHeight = 16.sp,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary)
            )
        }
        RatingIcon(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp, bottom = 15.dp),
            rating = product.rating,
            textStyle = MaterialTheme.typography.titleSmall,
            iconSize = 20.dp
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