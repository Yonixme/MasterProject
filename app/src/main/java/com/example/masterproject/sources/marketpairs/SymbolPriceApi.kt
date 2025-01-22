package com.example.masterproject.sources.marketpairs

import retrofit2.http.Query
import com.example.masterproject.sources.marketpairs.entities.KlineResponseEntity
import com.example.masterproject.sources.marketpairs.entities.PriceResponseEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface SymbolPriceApi {

    /**
     * [getMarketPrice] might return an array of elements, so you may need to change the return type
     */
    @GET(value = "ticker/price")
    suspend fun getMarketPrice() : List<PriceResponseEntity>

    /**
     * [getInfoForSymbolForDay] returns a array of arrays
     *
     *
     *     [
     *       [1737331200000,
     *       "101331.57000000",
     *       "109588.00000000",
     *       "99550.00000000",
     *       "107072.99000000",
     *       "48822.36240200",
     *       1737417599999,
     *       "5147208178.00621490",
     *       5920059, "24860.90120200",
     *       "2623122668.57160300",
     *       "0"]
     *      ]
     *
     *
     *    if write this array in data class
     *
     *    data class KlineResponseEntity(
     *     val openTime: Long,
     *     val open: String,
     *     val high: String,
     *     val low: String,
     *     val close: String,
     *     val volume: String,
     *     val closeTime: Long,
     *     val quoteAssetVolume: String,
     *     val numberOfTrades: Int,
     *     val takerBuyBaseAssetVolume: String,
     *     val takerBuyQuoteAssetVolume: String,
     *     val ignore: String)
     *
     */
    @GET(value = "klines")
    suspend fun getInfoForSymbolForDay(
        @Query("symbol") tradePair: String,
        @Query("interval") interval: String = "1d",
        @Query("limit") limit: Int = 1): Array<Array<Any>>
}