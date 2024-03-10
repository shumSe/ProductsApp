package ru.shumikhin.products.core.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun ShowErrorMessage(
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
        Text(
            text = message,
            style = textStyle.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.wrapContentSize(),
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedButton(
            onClick = onClickRetry,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(10.dp))
                .wrapContentSize()
        ) {
            Text(
                text = "Retry",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error
                )
            )
        }
    }
}