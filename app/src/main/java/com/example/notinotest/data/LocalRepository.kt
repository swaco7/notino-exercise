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

    @WorkerThread
    suspend fun deleteFavorite(productId: ProductId) {
        productDao.deleteFromFavorites(productId)
    }

    @WorkerThread
    suspend fun insertFavorite(productId: ProductId) {
        productDao.insertToFavorites(productId)
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