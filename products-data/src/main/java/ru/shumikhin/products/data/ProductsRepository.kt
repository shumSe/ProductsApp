package ru.shumikhin.products.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import ru.shumikhin.products.data.model.Product
import ru.shumikhin.products.data.model.toProduct
import ru.shumikhin.productsapi.ProductsApi
import ru.shumikhin.productsapi.models.ProductDTO
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val productsApi: ProductsApi,
) {

    fun getAllProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                ProductRemotePagingSource(productsApi)
            }
        ).flow
            .map { pagingData ->
                pagingData.map {
                    it.toProduct()
                }
            }.flowOn(Dispatchers.IO)
    }

    suspend fun getProductInfo(id: Int): Flow<RequestResult<Product>> {
        val start = flowOf<RequestResult<ProductDTO>>(RequestResult.Loading)
        val product = flow { emit(productsApi.productInfo(id)) }
            .map {
                it.toRequestResult()
            }.flowOn(Dispatchers.IO)
        return merge(start, product).map { requestResult ->
            requestResult.map {
                it.toProduct()
            }
        }
    }

}


sealed class RequestResult<out E : Any> {
    data object Loading : RequestResult<Nothing>()
    class Success<out E : Any>(val data: E) : RequestResult<E>()
    class Error(val error: Throwable? = null) : RequestResult<Nothing>()
}


fun <T : Any> Result<T>.toRequestResult(): RequestResult<T> {
    return when {
        isSuccess -> RequestResult.Success(data = this.getOrThrow())
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
