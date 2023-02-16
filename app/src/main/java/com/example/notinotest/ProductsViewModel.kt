package com.example.notinotest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
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

    private fun deleteFromFavorites(productId: Int) = viewModelScope.launch {
        localRepository.deleteFavorite(ProductId(productId))
    }

    private fun insertToFavorites(productId: Int) = viewModelScope.launch {
        localRepository.insertFavorite(ProductId(productId))
    }

    init {
        Log.e("init", "viewModel")
        viewModelScope.launch {
            getProducts()
        }
    }

    suspend fun getProducts() {
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
        }
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
}