package com.d121211017.stroyappsubmission.data.remote.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(token: String = "") : ApiService{
            val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client : OkHttpClient

            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(requestHeaders)
            }

            client = if(token == ""){
                OkHttpClient
                    .Builder()
                    .addInterceptor(logging)
                    .build()
            } else {
                OkHttpClient
                    .Builder()
                    .addInterceptor(logging)
                    .addInterceptor(authInterceptor)
                    .build()
            }

            val retrofit = Retrofit
                .Builder()
                .baseUrl("https://story-api.dicoding.dev/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}