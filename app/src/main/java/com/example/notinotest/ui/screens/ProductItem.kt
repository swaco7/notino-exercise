package com.example.notinotest.ui.screens

import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.widget.RatingBar
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.notinotest.Config
import com.example.notinotest.R
import com.example.notinotest.data.ProductById
import com.example.notinotest.data.ProductId

@Composable
fun ProductItem(
    product: ProductById,
    addToFavorites: (Int) -> Unit,
    removeFromFavorites: (Int) -> Unit,
    currentFavorites: List<ProductId>
){
    val isDark = isSystemInDarkTheme()
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
            val stars = ratingBar.progressDrawable as LayerDrawable
            stars.getDrawable(2).setColorFilter(
                context.resources.getColor(
                    if (isDark) R.color.white else R.color.gray_dark
                ),
                PorterDuff.Mode.SRC_ATOP
            )
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
            .clickable { }
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