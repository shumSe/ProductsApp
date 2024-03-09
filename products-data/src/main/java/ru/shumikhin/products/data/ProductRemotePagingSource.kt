package ru.shumikhin.products.data

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.shumikhin.productsapi.ProductsApi
import ru.shumikhin.productsapi.models.ProductDTO
import java.io.IOException

class ProductRemotePagingSource(
    private val productsApi: ProductsApi,
) : PagingSource<Int, ProductDTO>() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductDTO> {
        return try {
            val position = params.key ?: 1
            val response = productsApi.products(limit = 20, skip = 20 * (position-1) )
            val prevKey = if (position == 1) null else position - 1
            val nextKey = if (response.skip >= response.total || response.products.isEmpty()) null else position + 1

            LoadResult.Page(
                data = response.products,
                prevKey = prevKey,
                nextKey = nextKey,
            )

        }catch (e: IOException){
            LoadResult.Error(e)
        } catch (e: HttpException){
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, ProductDTO>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }


}