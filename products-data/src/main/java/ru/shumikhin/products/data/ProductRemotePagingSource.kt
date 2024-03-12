package ru.shumikhin.products.data

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.shumikhin.products.data.utils.ApiCallType
import ru.shumikhin.productsapi.ProductsApi
import ru.shumikhin.productsapi.models.ProductDTO

class ProductRemotePagingSource(
    private val productsApi: ProductsApi,
    private val searchType: ApiCallType = ApiCallType.Default,
) : PagingSource<Int, ProductDTO>() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductDTO> {
        val position = params.key ?: 1
        val response = when (searchType) {
            is ApiCallType.Category ->
                productsApi.productsByCategory(
                    searchType.categoryName,
                    limit = 20,
                    skip = 20 * (position - 1)
                )

            ApiCallType.Default ->
                productsApi.products(limit = 20, skip = 20 * (position - 1))

            is ApiCallType.Search ->
                productsApi.search(
                    searchType.searchParameter,
                    limit = 20,
                    skip = 20 * (position - 1)
                )
        }
        return when {
            response.isSuccess -> {
                val result = response.getOrThrow()
                val prevKey = if (position == 1) null else position - 1
                val nextKey =
                    if (result.skip >= result.total || result.products.isEmpty()) null else position + 1

                LoadResult.Page(
                    data = result.products,
                    prevKey = prevKey,
                    nextKey = nextKey,
                )
            }

            response.isFailure -> {
                LoadResult.Error(response.exceptionOrNull() as Throwable)
            }
            else -> LoadResult.Error(UnknownError())
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductDTO>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }


}