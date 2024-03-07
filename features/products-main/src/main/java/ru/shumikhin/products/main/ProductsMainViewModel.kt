package ru.shumikhin.products.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.shumikhin.products.data.RequestResult
import javax.inject.Inject

class ProductsMainViewModel @Inject constructor(
    getAllProductsUseCase: GetAllProductsUseCase,
): ViewModel() {

    val state: StateFlow<State> = getAllProductsUseCase().map {
        it.toState()
    }.stateIn(viewModelScope, SharingStarted.Lazily, State.Default)

}


private fun RequestResult<List<ProductUI>>.toState(): State {
    return when(this){
        is RequestResult.Error -> State.Error
        is RequestResult.Loading -> State.Loading
        is RequestResult.Success -> State.Success(data)
    }
}

sealed class State{
    data object Default: State()
    data object Error : State()
    class Success(val products: List<ProductUI>): State()
    data object Loading : State()
}