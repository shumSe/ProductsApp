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
import ru.shumikhin.products.data.utils.*
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

    fun getAllCategories(): Flow<RequestResult<List<String>>>{
        val start = flowOf<RequestResult<List<String>>>(RequestResult.Loading)
        val product = flow { emit(productsApi.categories()) }
            .map {
                it.toRequestResult()
            }.flowOn(Dispatchers.IO)
        return merge(start, product)
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

    fun searchProduct(searchText: String): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                ProductRemotePagingSource(
                    productsApi = productsApi,
                    searchType = ApiCallType.Search(searchParameter = searchText)
                )
            }
        ).flow
            .map { pagingData ->
                pagingData.map {
                    it.toProduct()
                }
            }.flowOn(Dispatchers.IO)
    }

    fun getByCategory(categoryName: String): Flow<PagingData<Product>>{
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                ProductRemotePagingSource(
                    productsApi = productsApi,
                    searchType = ApiCallType.Category(categoryName = categoryName),
                )
            }
        ).flow
            .map { pagingData ->
                pagingData.map {
                    it.toProduct()
                }
            }.flowOn(Dispatchers.IO)
    }

}


