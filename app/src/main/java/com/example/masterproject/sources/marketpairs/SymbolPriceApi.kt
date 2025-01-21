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
     * [getInfoForSymbolForDay] gets a  */
    @GET(value = "klines")
    suspend fun getInfoForSymbolForDay(
        @Query("symbol") tradePair: String,
        @Query("interval") interval: String = "1d",
        @Query("limit") limit: Int = 1): Array<Array<Any>>//List<KlineResponseEntity>
}