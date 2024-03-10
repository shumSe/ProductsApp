package ru.shumikhin.products.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.shumikhin.products.data.ProductsRepository
import ru.shumikhin.products.data.utils.RequestResult
import javax.inject.Inject

@HiltViewModel
class ProductsSearchViewModel @Inject constructor(
    private val repository: ProductsRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Default)
    val state = _state.asStateFlow()
    private val searchParameter: String = checkNotNull(savedStateHandle["searchParameter"])
    var searchFieldValue by mutableStateOf(searchParameter)
        private set

    fun updateSearchFieldValue(input: String) {
        searchFieldValue = input
    }

    fun retryLoadCategories(){
        viewModelScope.launch {
            loadCategories()
        }
    }

    private suspend fun loadCategories(){
        repository.getAllCategories().collect{
            _state.value = it.toState()
        }
    }

    init {
        viewModelScope.launch {
            loadCategories()
        }
    }

}

sealed class State {
    data object Default : State()
    class Error(val message: String) : State()
    class Success(val categories: List<String>) : State()
    data object Loading : State()
}

private fun RequestResult<List<String>>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error(error)
        is RequestResult.Loading -> State.Loading
        is RequestResult.Success -> State.Success(data)
    }
}