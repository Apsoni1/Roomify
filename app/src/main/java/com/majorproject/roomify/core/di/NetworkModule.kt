package com.majorproject.roomify.core.di

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.gson.GsonBuilder
import com.majorproject.roomify.core.network.constants.baseUrlOpenAI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addNetworkInterceptor(
            Interceptor { chain ->
                var request: Request? = null
                val original = chain.request()
                val prodServiceKey = Firebase.remoteConfig.getString("openai_key")
                val requestBuilder = original.newBuilder()
                    .addHeader("Authorization", "Bearer $prodServiceKey")
                request = requestBuilder.build()
                chain.proceed(request)
            })
        .addInterceptor(
            Interceptor { chain ->
                var attempt = 0
                val maxRetries = 3
                val retryDelayMillis = 500L
                var response: okhttp3.Response
                var exception: IOException? = null

                do {
                    try {
                        response = chain.proceed(chain.request())
                        if (response.isSuccessful) {
                            return@Interceptor response
                        }
                        response.close()
                    } catch (e: IOException) {
                        exception = e
                    }
                    attempt++
                    if (attempt < maxRetries) {
                        try {
                            Thread.sleep(retryDelayMillis * attempt) // Exponential backoff
                        } catch (interruptedException: Exception) {
                            Thread.currentThread().interrupt()
                            throw IOException("Retry interrupted", interruptedException)
                        }
                    }
                } while (attempt < maxRetries)

                // Throw the last exception if max retries exceeded
                throw exception ?: IOException("Unknown error during retry")
            }
        )
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder()
            .baseUrl(baseUrlOpenAI)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}