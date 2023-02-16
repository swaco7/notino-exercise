package com.example.notinotest

import retrofit2.Response
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val remoteService : RemoteService
) {
    suspend fun getProducts(): Response<Products> =
        remoteService.getProducts()
}