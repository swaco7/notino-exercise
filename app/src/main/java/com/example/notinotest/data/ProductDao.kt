package com.example.notinotest.data

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

    @Query("SELECT * FROM product_cart_table")
    fun getAllCart(): Flow<MutableList<ProductIdCart>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToCart(productId: ProductIdCart)

    @Delete
    suspend fun deleteFromCart(vararg products: ProductIdCart)
}