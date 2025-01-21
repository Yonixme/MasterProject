package com.example.masterproject.sources.marketpairs.entities

/**
 *[
 * ...
 *   {
 *     "symbol": "ETHBTC",
 *     "price": "0.03170000"
 *   },
 * ...
 * ]
 * */


data class PriceResponseEntity(
    val symbol: String,
    val price: Double
)