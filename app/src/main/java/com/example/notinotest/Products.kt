package com.example.notinotest

import com.google.gson.annotations.SerializedName

data class Products(
    @SerializedName("vpProductByIds")
    val vpProductByIds: List<VpProductById>
)

data class VpProductById(
    @SerializedName("productId")
    val productId: Int,
    @SerializedName("brand")
    val brand: Brand,
    @SerializedName("attributes")
    val attributes: Attributes,
    @SerializedName("annotation")
    val annotation: String,
    @SerializedName("masterId")
    val masterId: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("orderUnit")
    val orderUnit: String,
    @SerializedName("price")
    val price: Price,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("productCode")
    val productCode: String,
    @SerializedName("reviewSummary")
    val reviewSummary: ReviewSummary,
    @SerializedName("stockAvailability")
    val stockAvailability: StockAvailability,
)

data class Brand(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)

data class Attributes(
    @SerializedName("Action")
    val action: Boolean?,
    @SerializedName("AirTransportDisallowed")
    val airTransportDisallowed: Boolean?,
    @SerializedName("DifferentPackaging")
    val differentPackaging: Boolean?,
    @SerializedName("FreeDelivery")
    val freeDelivery: Boolean?,
    @SerializedName("Master")
    val master: Boolean,
    @SerializedName("New")
    val new: Boolean?,
    @SerializedName("PackageSize")
    val packageSize: PackageSize
)

data class Price(
    @SerializedName("value")
    val value: Int,
    @SerializedName("currency")
    val currency: String,
)

data class ReviewSummary(
    @SerializedName("score")
    val score: Int,
    @SerializedName("averageRating")
    val averageRating: Double,
)

data class StockAvailability(
    @SerializedName("code")
    val code: String,
    @SerializedName("count")
    val count: Any?
)

data class PackageSize(
    @SerializedName("height")
    val height: Int,
    @SerializedName("width")
    val width: Int,
    @SerializedName("depth")
    val depth: Int,
)
