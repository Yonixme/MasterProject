package com.example.masterproject.sources.marketpairs.entities

/**
 * [1737331200000, "101331.57000000",
 *     "109588.00000000",
 *     "99550.00000000",
 *     "107072.99000000",
 *     "48822.36240200",
 *     1737417599999, "5147208178.00621490",
 *     5920059, "24860.90120200",
 *     "2623122668.57160300",
 *     "0"
 *   ]
 * */

data class KlineResponseEntity(
    val openTime: Long,
    val open: String,
    val high: String,
    val low: String,
    val close: String,
    val volume: String,
    val closeTime: Long,
    val quoteAssetVolume: String,
    val numberOfTrades: Int,
    val takerBuyBaseAssetVolume: String,
    val takerBuyQuoteAssetVolume: String,
    val ignore: String
)