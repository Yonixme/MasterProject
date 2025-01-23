package com.example.masterproject.model.marketpair.database

import com.example.masterproject.model.marketpair.database.entities.MarketPairDbEntity
import com.example.masterproject.model.marketpair.entities.MarketPair
import kotlinx.coroutines.flow.Flow

interface DBMarketPairRepository {
    fun getAllMarketPairs(): Flow<List<MarketPair?>>

    suspend fun deleteMarketPair(id: Long)

    suspend fun addMarketPair(marketPair: MarketPair)

    fun setIgnoreSavingFlagForMarketPair(marketPair: MarketPair)

    suspend fun clearTable()

}