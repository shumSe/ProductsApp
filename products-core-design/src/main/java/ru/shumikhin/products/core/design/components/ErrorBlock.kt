package ru.shumikhin.products.core.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ErrorBlock(
    modifier: Modifier = Modifier,
    mainText: String,
    errorMessage: String = "",
    textStyle: TextStyle,
    retryAction: () -> Unit,
){
    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Text(
            text = mainText,
            style = textStyle,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(5.dp))
        ShowErrorMessage(
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth(),
            message = errorMessage,
            onClickRetry = retryAction
        )
    }
}