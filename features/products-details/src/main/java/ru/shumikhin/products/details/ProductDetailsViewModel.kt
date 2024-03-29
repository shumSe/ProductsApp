package ru.shumikhin.products.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.shumikhin.products.data.utils.RequestResult
import ru.shumikhin.products.details.model.ProductDetailsUI
import ru.shumikhin.products.details.utils.DETAILS_SCREEN_ARGUMENT_ID
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val productId: Int = checkNotNull(savedStateHandle[DETAILS_SCREEN_ARGUMENT_ID])

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Default)

    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            loadProductItem()
        }
    }

    fun retryLoadProduct(){
        viewModelScope.launch {
            loadProductItem()
        }
    }

    private suspend fun loadProductItem() {
        getProductDetailsUseCase(productId).map { it.toState() }.collect {
            _state.value = it
        }
    }

}

private fun RequestResult<ProductDetailsUI>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error(error)
        is RequestResult.Loading -> State.Loading
        is RequestResult.Success -> State.Success(data)
    }
}

sealed class State {
    data object Default : State()
    class Error(val message: String) : State()
    class Success(val product: ProductDetailsUI) : State()
    data object Loading : State()
}