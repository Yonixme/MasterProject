package com.example.masterproject.model.marketpair.entities

data class MarketPair(
    val id: Long,
    val tradePair: String,
    val sourceName: String,
    val ignoreWhenSaving: Boolean
){
    fun toMarketPairWithDetails(price: Double): MarketPairWithDetails{
        return MarketPairWithDetails(
            id = id,
            tradePair = tradePair,
            sourceName = sourceName,
            price = price,
            ignoreWhenSaving = ignoreWhenSaving
        )
    }

    companion object{
        fun fromMarketPairWithDetails(marketPairWithDetails: MarketPairWithDetails): MarketPair{
            return MarketPair(
                id = marketPairWithDetails.id,
                tradePair = marketPairWithDetails.tradePair,
                sourceName = marketPairWithDetails.sourceName,
                ignoreWhenSaving = marketPairWithDetails.ignoreWhenSaving
            )
        }
    }
}

data class MarketPairWithDetails(
    val id: Long,
    val sourceName: String,
    val tradePair: String,
    val ignoreWhenSaving: Boolean,
    val price: Double,
){
    fun toMarketPair(): MarketPair{
        return MarketPair(
            id = id,
            tradePair = tradePair,
            sourceName = sourceName,
            ignoreWhenSaving = ignoreWhenSaving
        )
    }

    companion object{
        fun fromMarketPairAndPrice(marketPair: MarketPair, price: Double): MarketPairWithDetails{
            return MarketPairWithDetails(
                id = marketPair.id,
                tradePair = marketPair.tradePair,
                sourceName = marketPair.sourceName,
                ignoreWhenSaving = marketPair.ignoreWhenSaving,
                price = price
            )
        }
    }
}