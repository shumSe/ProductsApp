package ru.shumikhin.products.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.shumikhin.products.data.RequestResult
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val productId: Int = checkNotNull(savedStateHandle["id"])

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Default)

    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            loadItem()
        }
    }

    fun retryLoad(){
        viewModelScope.launch {
            loadItem()
        }
    }

    private suspend fun loadItem() {
        getProductDetailsUseCase(productId).map { it.toState() }.collect {
            _state.value = it
        }
    }

}

private fun RequestResult<ProductDetailsUI>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error
        is RequestResult.Loading -> State.Loading
        is RequestResult.Success -> State.Success(data)
    }
}

sealed class State {
    data object Default : State()
    data object Error : State()
    class Success(val product: ProductDetailsUI) : State()
    data object Loading : State()
}