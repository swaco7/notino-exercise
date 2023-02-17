package com.example.notinotest.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_cart_table")
data class ProductIdCart(
    @PrimaryKey val id : Int
)
