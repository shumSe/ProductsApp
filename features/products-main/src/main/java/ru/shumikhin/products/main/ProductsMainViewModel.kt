package ru.shumikhin.products.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.shumikhin.products.data.RequestResult
import javax.inject.Inject
@HiltViewModel
class ProductsMainViewModel @Inject constructor(
    getAllProductsUseCase: GetAllProductsUseCase,
): ViewModel() {

//    val state: StateFlow<State> = getAllProductsUseCase().map {
//        it.
//    }.stateIn(viewModelScope, SharingStarted.Lazily, State.Default)

    private val _productResponse: MutableStateFlow<PagingData<ProductUI>> =
        MutableStateFlow(PagingData.empty())
    var productResponse = _productResponse.asStateFlow()
        private set

    init{
        viewModelScope.launch {
            getAllProductsUseCase().collect{ pagingData ->
                _productResponse.value = pagingData
            }
        }
    }

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