package com.example.notinotest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ProductsApp : Application() {
   // val database by lazy { Database.getDatabase(this) }
//    val localRepository by lazy { LocalRepository(database.productDao()) }
}