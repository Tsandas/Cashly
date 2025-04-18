package com.example.financeapptestversion.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class MStockItem(
    @Exclude var id: String? = null,
    val date: String,
    val price: Double,
    val symbol: String,
    val volume: Int,
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = null,
    @get:PropertyName("price_bought")
    @set:PropertyName("price_bought")
    var priceBought: Double? = null,
    @get:PropertyName("quantity_bought")
    @set:PropertyName("quantity_bought")
    var quantityBought: Int? = null
)