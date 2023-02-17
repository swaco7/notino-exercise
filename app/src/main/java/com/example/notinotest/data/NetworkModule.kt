package com.example.notinotest.data

import android.util.Log
import com.example.notinotest.Config.baseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val client = OkHttpClient
        .Builder()
        .addInterceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .build()
            Log.e("request", request.toString())
            chain.proceed(request)
        }
        .build()

    private val notinoRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideRetrofit(): RemoteService = notinoRetrofit
        .create(RemoteService::class.java)

}
