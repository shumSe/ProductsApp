package ru.shumikhin.products.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.shumikhin.products.main.utils.RequestType
import javax.inject.Inject

@HiltViewModel
class ProductsMainViewModel @Inject constructor(
    getAllProductsUseCase: GetAllProductsUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val requestType: RequestType = RequestType.entries[checkNotNull(savedStateHandle["type"])]
    private val searchParameter: String = checkNotNull(savedStateHandle["searchArgument"])

    private val _productResponse: MutableStateFlow<PagingData<ProductUI>> =
        MutableStateFlow(PagingData.empty())
    var productResponse = _productResponse.asStateFlow()
        private set

    init{
        viewModelScope.launch{
            getAllProductsUseCase(requestType = requestType, searchParameter = searchParameter).cachedIn(viewModelScope).collect{ pagingData ->
                _productResponse.value = pagingData
            }
        }
    }

}

