package com.github.moviesapi.di

import com.github.moviesapi.BuildConfig
import com.github.moviesapi.network.DiscoverApi
import com.github.moviesapi.repository.MoviesListDataSource
import com.github.moviesapi.repository.MoviesListRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object HiltModules {

    @Provides
    @Singleton
    @UnstableDefault
    fun provideRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .retryOnConnectionFailure(true).addInterceptor(interceptor).build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideAPI(retrofit: Retrofit): DiscoverApi =
        retrofit.create(DiscoverApi::class.java)

    @Provides
    fun provideMoviesListDataSource(api: DiscoverApi) =
        MoviesListDataSource(api)

    @Singleton
    @Provides
    fun provideMoviesRepository(dataSource: MoviesListDataSource) =
        MoviesListRepository(dataSource)


}