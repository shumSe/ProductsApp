package ru.shumikhin.products.main

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.shumikhin.products.data.ProductsRepository
import ru.shumikhin.products.data.RequestResult
import ru.shumikhin.products.data.map
import javax.inject.Inject
import ru.shumikhin.products.data.model.Product as DataProduct

class GetAllProductsUseCase @Inject constructor(
    private val repository: ProductsRepository,
) {
    operator fun invoke(): Flow<RequestResult<List<ProductUI>>> {
return repository.getAllProducts()
    .map {reqResult ->
        reqResult.map {repoProducts ->
            repoProducts.map {
                it.toUiProduct()
            }
        }
    }
    }
}

private fun DataProduct.toUiProduct(): ProductUI{
    return ProductUI(
        id = id,
        title = title,
        description = description,
        price = price,
        rating = rating,
        thumbnail = thumbnail,
    )
}