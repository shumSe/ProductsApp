package ru.shumikhin.productsapi

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import ru.shumikhin.productsapi.models.ProductDTO
import ru.shumikhin.productsapi.models.Response

interface ProductsApi {
    @GET("/products")
    suspend fun products(
        @Query("skip") skip: Int? = null,
        @Query("limit") limit: Int? = null,
    ): Result<Response<ProductDTO>>
}


fun ProductsApi(
    baseUrl: String,
    json: Json = Json,
): ProductsApi {
    return retrofit(baseUrl = baseUrl,json = json).create()
}

private fun retrofit(
    baseUrl: String,
    json: Json,
): Retrofit {
    val jsonConverterFactory = json.asConverterFactory(MediaType.get("application/json"))

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(jsonConverterFactory)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .build()
}

