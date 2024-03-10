package ru.shumikhin.products.core.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RatingIcon(modifier: Modifier = Modifier, rating: Float = 1.6f, textStyle: TextStyle = TextStyle.Default, iconSize: Dp,) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Icon(
            imageVector = Icons.Outlined.Star,
            contentDescription = "Rating Icon",
            tint = Color.Yellow.copy(green=0.8f),
            modifier = Modifier.size(iconSize)
        )
        Spacer(modifier = Modifier.size(2.dp))
        Text(
            text = rating.toString(),
            style = textStyle,
        )
    }
}