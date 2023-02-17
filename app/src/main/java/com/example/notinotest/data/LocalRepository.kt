package com.example.notinotest.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val productDao: ProductDao
) {
    val allFavorites: Flow<MutableList<ProductId>> = productDao.getAll()
    val allCart: Flow<MutableList<ProductIdCart>> = productDao.getAllCart()

    @WorkerThread
    suspend fun deleteFavorite(productId: ProductId) {
        productDao.deleteFromFavorites(productId)
    }

    @WorkerThread
    suspend fun insertFavorite(productId: ProductId) {
        productDao.insertToFavorites(productId)
    }

    @WorkerThread
    suspend fun deleteFromCart(productId: ProductIdCart) {
        productDao.deleteFromCart(productId)
    }

    @WorkerThread
    suspend fun insertToCart(productId: ProductIdCart) {
        productDao.insertToCart(productId)
    }

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: LocalRepository? = null

        fun getInstance(productDao: ProductDao) =
            instance ?: synchronized(this) {
                instance ?: LocalRepository(productDao).also { instance = it }
            }
    }
}