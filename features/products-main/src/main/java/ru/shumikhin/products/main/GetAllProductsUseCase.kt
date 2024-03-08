package ru.shumikhin.products.main

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.shumikhin.products.data.ProductsRepository
import javax.inject.Inject
import ru.shumikhin.products.data.model.Product as DataProduct

class GetAllProductsUseCase @Inject constructor(
    private val repository: ProductsRepository,
) {
    operator fun invoke(): Flow<PagingData<ProductUI>> {
        return repository.getAllProducts()
            .map { pagingData ->
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