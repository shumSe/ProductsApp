package ru.shumikhin.products.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import ru.shumikhin.products.data.model.Product
import ru.shumikhin.products.data.model.toProduct
import ru.shumikhin.productsapi.ProductsApi
import ru.shumikhin.productsapi.models.ProductDTO
import ru.shumikhin.productsapi.models.Response

class ProductsRepository(
    private val productsApi: ProductsApi,
) {

    fun getAllProducts(): Flow<RequestResult<List<Product>>> {
        val start = flowOf<RequestResult<Response<ProductDTO>>>(RequestResult.Loading)
        val remoteData  = flow { emit(productsApi.products()) }.map {
            it.toRequestResult()
        }
        return merge(remoteData,start).map { result ->
            result.map {response ->
                response.products.map { productDto ->
                    productDto.toProduct()
                }
            }
        }
    }

}




sealed class RequestResult<out E: Any>{
    data object Loading : RequestResult<Nothing>()
    class Success<out E: Any>(val data: E) : RequestResult<E>()
    class Error(val error: Throwable? = null) : RequestResult<Nothing>()
}
fun <T : Any> Result<T>.toRequestResult(): RequestResult<T> {
    return when {
        isSuccess -> RequestResult.Success(this.getOrThrow())
        isFailure -> RequestResult.Error(error = this.exceptionOrNull())
        else -> error("Impossible branch")
    }
}

fun <I : Any, O : Any> RequestResult<I>.map(mapper: (I) -> O): RequestResult<O> {
    return when (this) {
        is RequestResult.Success -> RequestResult.Success(mapper(data))
        is RequestResult.Error -> RequestResult.Error()
        is RequestResult.Loading -> RequestResult.Loading
    }
}
