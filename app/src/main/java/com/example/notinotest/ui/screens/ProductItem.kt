package com.example.notinotest.ui.screens

import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.widget.RatingBar
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.notinotest.data.ProductIdCart
import com.example.notinotest.ui.theme.InkTertiary

@Composable
fun ProductItem(
    product: ProductById,
    addToFavorites: (Int) -> Unit,
    removeFromFavorites: (Int) -> Unit,
    addToCart: (Int) -> Unit,
    removeFromCart: (Int) -> Unit,
    currentFavorites: List<ProductId>,
    currentCart: List<ProductIdCart>
){
    val isDark = isSystemInDarkTheme()
    val isFavorite = currentFavorites.find { it.id == product.productId } != null
    val isInCart = currentCart.find { it.id == product.productId } != null
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
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            Image(
                painter = if (!isFavorite) {
                    painterResource(id = R.drawable.heart)
                } else {
                    painterResource(id = R.drawable.heart_filled)
                },
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        if (!isFavorite) {
                            addToFavorites(product.productId)
                        } else {
                            removeFromFavorites(product.productId)
                        }
                    }
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
            .clickable {
                if (!isInCart) {
                    addToCart(product.productId)
                } else {
                    removeFromCart(product.productId)
                }
            }
        ) {
            Text(
                text = if (!isInCart) stringResource(R.string.to_cart) else stringResource(R.string.in_cart),
                textAlign = TextAlign.Center,
                style = if (!isInCart) MaterialTheme.typography.h4 else MaterialTheme.typography.h4.copy(color = InkTertiary),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            )
        }
    }
}