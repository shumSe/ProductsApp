package ru.shumikhin.products.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import ru.shumikhin.products.core.design.components.SearchField
import ru.shumikhin.products.core.design.components.ShowError
import ru.shumikhin.products.core.design.components.ShowLoading

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductSearch(
    viewModel: ProductsSearchViewModel = hiltViewModel(),
    onFindClick: (searchParams: String) -> Unit = {},
    onCategoryClick: (categoryName: String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Column(
        Modifier
            .padding(horizontal = 10.dp)
            .fillMaxSize()) {
        SearchField(
            text = viewModel.searchFieldValue,
            onTextChange = { viewModel.updateSearchFieldValue(it) },
            modifier = Modifier.focusRequester(focusRequester),
            onDoneClick = onFindClick,
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = "Categories",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(10.dp))
        when (val currentState = state) {
            State.Default -> Text(text = "No categories")
            State.Error -> ShowError(modifier = Modifier.fillMaxWidth()) {
                coroutineScope.launch {  viewModel.retryLoadCategories() }
            }
            State.Loading -> ShowLoading(modifier = Modifier.fillMaxWidth())
            is State.Success -> {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    currentState.categories.forEach { categoryName ->
                        SuggestionChip(onClick = {onCategoryClick(categoryName)}, label = { Text(
                            text = categoryName
                        )})
                    }
                }
            }
        }
    }
}