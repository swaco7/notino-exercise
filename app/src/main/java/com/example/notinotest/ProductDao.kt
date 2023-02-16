package com.example.notinotest

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM product_table")
    fun getAll(): Flow<MutableList<ProductId>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToFavorites(productId: ProductId)

    @Delete
    suspend fun deleteFromFavorites(vararg products: ProductId)
}