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
import ru.shumikhin.products.main.models.ProductUI
import ru.shumikhin.products.main.utils.ALL_PRODUCTS
import ru.shumikhin.products.main.utils.CATEGORY_PRODUCTS
import ru.shumikhin.products.main.utils.HOME_SCREEN_ARGUMENT_SEARCH
import ru.shumikhin.products.main.utils.HOME_SCREEN_ARGUMENT_TYPE
import ru.shumikhin.products.main.utils.SEARCH_PRODUCTS
import ru.shumikhin.products.main.utils.ScreenType
import javax.inject.Inject

@HiltViewModel
class ProductsMainViewModel @Inject constructor(
    getAllProductsUseCase: GetAllProductsUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val searchParameter: String = checkNotNull(savedStateHandle[HOME_SCREEN_ARGUMENT_SEARCH])

    private val screenType: ScreenType = when(checkNotNull(savedStateHandle[HOME_SCREEN_ARGUMENT_TYPE])){
        ALL_PRODUCTS -> ScreenType.All
        SEARCH_PRODUCTS -> ScreenType.Search(sParam = searchParameter)
        CATEGORY_PRODUCTS -> ScreenType.Category(category = searchParameter)
        else -> ScreenType.All
    }


    private val _productResponse: MutableStateFlow<PagingData<ProductUI>> =
        MutableStateFlow(PagingData.empty())
    var productResponse = _productResponse.asStateFlow()
        private set

    init{
        viewModelScope.launch{
            getAllProductsUseCase(requestType = screenType).cachedIn(viewModelScope).collect{ pagingData ->
                _productResponse.value = pagingData
            }
        }
    }

    fun getScreenType(): ScreenType{
        return screenType
    }

}
