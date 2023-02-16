package com.example.notinotest

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class ProductId(
    @PrimaryKey val id : Int
)
