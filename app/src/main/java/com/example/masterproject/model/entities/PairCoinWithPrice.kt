package com.example.masterproject.model.entities

data class PairCoinWithPrice(
    val id: Long,
    val pair: String,
    val sourceName: String,
    val price: Double
)

data class PairCoin(
    val id: Long,
    val pair: String,
    val sourceName: String,
)
