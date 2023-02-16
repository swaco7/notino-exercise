package com.example.notinotest

import android.util.Log
import android.view.LayoutInflater
import android.widget.RatingBar
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.notinotest.ui.theme.Teal200

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

@Composable
fun ProductItem(
    product: VpProductById,
    addToFavorites: (Int) -> Unit,
    removeFromFavorites: (Int) -> Unit,
    currentFavorites: List<ProductId>
){
    val isFavorite = currentFavorites.find { it.id == product.productId } != null
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(164.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.End)
                .height(48.dp)
                .width(48.dp)
                .clickable {
                    if (!isFavorite) {
                        addToFavorites(product.productId)
                    } else {
                        removeFromFavorites(product.productId)
                    }
                }
        ) {
            Image(
                painter = if (!isFavorite) {
                    painterResource(id = R.drawable.heart)
                } else {
                    painterResource(id = R.drawable.heart_filled)
                },
                contentDescription = "",
                modifier = Modifier.align(Alignment.Center)
            )
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("${Config.baseImageUrl}${product.imageUrl}")
                .crossfade(true)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .size(160.dp)
                .background(Color.White)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = product.brand.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = product.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(top = 4.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = product.annotation,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .align(Alignment.CenterHorizontally)
        )
        AndroidView(factory = { context ->
            val view = LayoutInflater.from(context)
                .inflate(R.layout.rating_bar, null, false)
            val ratingBar = view as RatingBar
            ratingBar.rating = product.reviewSummary.score.toFloat()
            ratingBar.numStars = 5
            view
        }, modifier = Modifier.align(Alignment.CenterHorizontally))
        Text(
            text = "${product.price.value} ${product.price.currency}",
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.CenterHorizontally)
        )
        Box(modifier = Modifier
            .padding(vertical = 12.dp)
            .border(
                1.dp,
                Color.Gray,
                RoundedCornerShape(2.dp)
            )
            .height(36.dp)
            .align(Alignment.CenterHorizontally)
            .clickable {  }
        ) {
            Text(
                text = stringResource(R.string.to_cart),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            )
        }
    }
}