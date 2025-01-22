package com.example.masterproject.model.marketpair.entities

data class MarketPair(
    val id: Long,
    val tradePair: String,
    val sourceName: String,
    val ignoreWhenSaving: Boolean
)

data class MarketPairWithDetails(
    val id: Long,
    val sourceName: String,
    val tradePair: String,
    val price: Double
)