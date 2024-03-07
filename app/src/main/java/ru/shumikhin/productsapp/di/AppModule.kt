package ru.shumikhin.productsapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.shumikhin.productsapi.ProductsApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideProductsApi(): ProductsApi{
        return ProductsApi(baseUrl = "https://dummyjson.com/")
    }

}