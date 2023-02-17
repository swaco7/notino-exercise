package com.example.notinotest.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.notinotest.R
import com.example.notinotest.ui.ProductsViewModel
import com.example.notinotest.data.ProductId
import com.example.notinotest.data.ProductIdCart
import com.example.notinotest.data.Products


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductList(
    viewModel: ProductsViewModel
) {
    val favorites by viewModel.favorites.observeAsState(initial = emptyList())
    val cart by viewModel.productsInCart.observeAsState(initial = emptyList())
    val refreshing = viewModel.isRefreshing.collectAsState().value
    val pullRefreshState = rememberPullRefreshState(refreshing, { viewModel.refresh() })

    Column {
        Box(modifier = Modifier.pullRefresh(pullRefreshState) ) {
            when (val state = viewModel.uiState.collectAsState().value) {
                is ProductsViewModel.LoadingState.Loaded
                -> ProductsLoaded(
                    data = state.data,
                    addToFavorites = { viewModel.addToFavorites(it) },
                    removeFromFavorites = { viewModel.removeFromFavorites(it) },
                    addToCart = { viewModel.addToCart(it) },
                    removeFromCart = { viewModel.removeFromCart(it) },
                    favorites = favorites,
                    cart = cart,
                )
                is ProductsViewModel.LoadingState.Loading -> Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //CircularProgressIndicator()
                }
                is ProductsViewModel.LoadingState.Error -> {
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        Box(Modifier.fillMaxSize()) {
                            Text(
                                text = stringResource(R.string.no_content),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                else -> {}
            }
            PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductsLoaded(
    data: Products,
    addToFavorites: (Int) -> Unit,
    removeFromFavorites: (Int) -> Unit,
    addToCart: (Int) -> Unit,
    removeFromCart: (Int) -> Unit,
    favorites: List<ProductId>,
    cart: List<ProductIdCart>
) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            items(data.vpProductByIds.size) { item ->
                ProductItem(
                    data.vpProductByIds[item],
                    addToFavorites,
                    removeFromFavorites,
                    addToCart,
                    removeFromCart,
                    favorites,
                    cart
                )
            }
        }
    }
}