package com.example.masterproject.model.marketpair.database

import com.example.masterproject.model.marketpair.database.entities.MarketPairDbEntity
import com.example.masterproject.model.marketpair.entities.MarketPair
import kotlinx.coroutines.flow.Flow

interface DBMarketPairRepository {
    fun getAllMarketPairs(sourceName: String): Flow<List<MarketPairDbEntity?>>

    suspend fun deleteMarketPair(id: Long)

    suspend fun AddMarketPair(marketPair: MarketPair)

}