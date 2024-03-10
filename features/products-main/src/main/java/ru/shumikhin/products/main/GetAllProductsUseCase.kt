package ru.shumikhin.products.main

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.shumikhin.products.data.ProductsRepository
import ru.shumikhin.products.main.models.ProductUI
import ru.shumikhin.products.main.utils.ScreenType
import javax.inject.Inject
import ru.shumikhin.products.data.model.Product as DataProduct

class GetAllProductsUseCase @Inject constructor(
    private val repository: ProductsRepository,
) {
    operator fun invoke(requestType: ScreenType): Flow<PagingData<ProductUI>> {
        return when(requestType){
            ScreenType.All -> repository.getAllProducts()
            is ScreenType.Search -> repository.searchProduct(requestType.searchParameter)
            is ScreenType.Category -> repository.getByCategory(requestType.categoryName)
        }.map { pagingData ->
                pagingData.map {
                    it.toUiProduct()
                }
            }
    }
}

private fun DataProduct.toUiProduct(): ProductUI {
    return ProductUI(
        id = id,
        title = title,
        description = description,
        price = price,
        rating = rating,
        thumbnail = thumbnail,
    )
}