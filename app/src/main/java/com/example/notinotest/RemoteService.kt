package com.example.notinotest

import retrofit2.Response
import retrofit2.http.GET


interface RemoteService {
    @GET("cernfr1993/notino-assignment/db")
    suspend fun getProducts(): Response<Products>
}