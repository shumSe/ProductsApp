package ru.shumikhin.products.core.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ShowError(
    modifier: Modifier = Modifier,
    message: String = "",
    textStyle: TextStyle = TextStyle.Default,
    onClickRetry: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, style = textStyle, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedButton(onClick =onClickRetry, modifier = Modifier.clip(shape= RoundedCornerShape(10.dp) ).wrapContentSize()) {
            Text(text = "Retry", style = TextStyle().copy(fontWeight = FontWeight.Bold ,color = Color.Red.copy(alpha = 0.8f)))
        }
    }
}