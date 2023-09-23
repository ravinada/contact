package com.ravinada.contact.data.retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ravinada.contact.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object NetworkBuilder {

    const val BASE_URL = BuildConfig.BASE_URL

    fun <T> create(
        baseUrl: String, apiType: Class<T>
    ): T =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(gmHttpClient())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(apiType)

    private fun gmHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(2, TimeUnit.MINUTES)
        builder.writeTimeout(2, TimeUnit.MINUTES)
        builder.readTimeout(2, TimeUnit.MINUTES)
        builder.addInterceptor(HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }).retryOnConnectionFailure(true)

        builder.addInterceptor { chain ->
            var request = chain.request()
            val requestBuilder = request.newBuilder()
            request = requestBuilder.build()
            chain.proceed(request)
        }
        return builder.build()
    }
}