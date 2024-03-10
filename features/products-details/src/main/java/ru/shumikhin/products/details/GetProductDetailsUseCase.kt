package ru.shumikhin.products.details

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.shumikhin.products.data.ProductsRepository
import ru.shumikhin.products.data.RequestResult
import ru.shumikhin.products.data.map
import ru.shumikhin.products.details.model.ProductDetailsUI
import javax.inject.Inject
import ru.shumikhin.products.data.model.Product as ProductData

class GetProductDetailsUseCase @Inject constructor(
    private val repository: ProductsRepository,
) {
    suspend operator fun invoke(id: Int): Flow<RequestResult<ProductDetailsUI>> {
        return repository.getProductInfo(id = id).map { requestResult ->
            requestResult.map {
                it.toProductDetailsUI()
            }
        }
    }

}

private fun ProductData.toProductDetailsUI(): ProductDetailsUI {
    return ProductDetailsUI(
        id = id,
        title = title,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand,
        category = category,
        thumbnail = thumbnail,
        images = images,
    )
}
