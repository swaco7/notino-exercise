package com.example.notinotest.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notinotest.ui.ProductsViewModel
import com.example.notinotest.data.ProductId
import com.example.notinotest.data.Products


@Composable
fun ProductList(
    viewModel: ProductsViewModel
) {
    val favorites by viewModel.favorites.observeAsState(initial = emptyList())
    Column {
        when (val state = viewModel.uiState.collectAsState().value) {
            is ProductsViewModel.LoadingState.Loaded
            -> ProductsLoaded(
                data = state.data,
                addToFavorites = { viewModel.addToFavorites(it) },
                removeFromFavorites = { viewModel.removeFromFavorites(it) },
                favorites = favorites,
            )
            is ProductsViewModel.LoadingState.Loading -> Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
            is ProductsViewModel.LoadingState.Error -> {}
            else -> {}
        }


    }
}

@Composable
fun ProductsLoaded(
    data: Products,
    addToFavorites: (Int) -> Unit,
    removeFromFavorites: (Int) -> Unit,
    favorites: List<ProductId>
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        items(data.vpProductByIds.size) { item ->
            ProductItem(data.vpProductByIds[item], addToFavorites, removeFromFavorites, favorites)
        }
    }
}