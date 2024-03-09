package ru.shumikhin.products.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProductDetails(
    viewModel: ProductDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    when(val currentState = state){
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
@Composable
private fun Loading(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        CircularProgressIndicator()
    }
}

@Composable
private fun Error(
    onRetry: () -> Unit,
){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text="Error", style = MaterialTheme.typography.titleLarge, color = Color.Red)
            Button(onClick = onRetry) {
                Text(text = "Retry")
            }
        }
    }
}
@Composable
private fun ProductDetailsContainer(
    product: ProductDetailsUI
){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = product.title, style = MaterialTheme.typography.titleLarge, color = Color.Blue.copy(alpha = 0.7f))
    }
}