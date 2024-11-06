package com.example.cal.di

import com.example.cal.data.CryptoManagerImpl
import com.example.cal.data.RecipesRepositoryImpl
import com.example.cal.data.network.RetrofitApiService
import com.example.cal.domain.CryptoManager
import com.example.cal.domain.RecipeRepository
import com.example.cal.domain.network.ApiClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        recipeRepositoryImpl: RecipesRepositoryImpl
    ): RecipeRepository

    @Binds
    @Singleton
    abstract fun bindApiClient(
        retrofitApiService: RetrofitApiService
    ): ApiClient

    @Binds
    @Singleton
    abstract fun bindCryptoManager(
        cryptoManagerImpl: CryptoManagerImpl
    ): CryptoManager
}