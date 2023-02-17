package com.example.notinotest.ui

import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.notinotest.R
import com.example.notinotest.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject internal constructor(
    private val repository: RemoteRepository,
    private val localRepository: LocalRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<LoadingState>(LoadingState.Empty)
    val uiState: StateFlow<LoadingState> = _uiState
    val favorites : LiveData<MutableList<ProductId>> = localRepository.allFavorites.asLiveData()
    val productsInCart : LiveData<MutableList<ProductIdCart>> = localRepository.allCart.asLiveData()
    val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing


    private fun deleteFromFavorites(productId: Int) = viewModelScope.launch {
        localRepository.deleteFavorite(ProductId(productId))
    }

    private fun insertToFavorites(productId: Int) = viewModelScope.launch {
        localRepository.insertFavorite(ProductId(productId))
    }

    private fun deleteFromCart(productId: Int) = viewModelScope.launch {
        localRepository.deleteFromCart(ProductIdCart(productId))
    }

    private fun insertToCart(productId: Int) = viewModelScope.launch {
        localRepository.insertToCart(ProductIdCart(productId))
    }

    init {
        Log.e("init", "viewModel")
        getProducts()
    }

    private fun getProducts() {
        _isRefreshing.value = true
        _uiState.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getProducts()
                if (response.isSuccessful) {
                    _uiState.value =
                        LoadingState.Loaded(response.body()!!)
                } else {
                    _uiState.value = LoadingState.Error("")
                }
            } catch (ex: Exception) {
                _uiState.value = LoadingState.Error("")
            }
            _isRefreshing.value = false
        }
    }

    fun refresh(){
        getProducts()
    }

    sealed class LoadingState {
        object Empty : LoadingState()
        object Loading : LoadingState()
        class Loaded(val data: Products) : LoadingState()
        class Error(val message: String) : LoadingState()
    }

    fun addToFavorites(productId: Int){
        insertToFavorites(productId)
    }

    fun removeFromFavorites(productId: Int){
        deleteFromFavorites(productId)
    }

    fun addToCart(productId: Int) {
        insertToCart(productId)
    }

    fun removeFromCart(productId: Int) {
        deleteFromCart(productId)
    }
}