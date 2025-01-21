package com.example.masterproject.model.marketsnapshot.database

import com.example.masterproject.model.marketpair.entities.MarketPairWithDetails
import com.example.masterproject.model.marketsnapshot.database.entities.MarketSnapshotDbEntity
import com.example.masterproject.model.marketsnapshot.entities.MarketSnapshot
import com.example.masterproject.model.marketsnapshot.entities.MarketSnapshotAndDetails
import kotlinx.coroutines.flow.Flow

interface DBMarketSnapshotRepository {

    //fun getMarketSnapshotAndDetails(): Flow<Map<MarketSnapshotDbEntity, List<MarketSnapshotDetailsDbEntity>>>
    fun getMarketSnapshotAndDetails(): Flow<List<MarketSnapshotAndDetails>>

    suspend fun getSnapshots(): List<MarketSnapshot>

    suspend fun setDetailsForMarketSnapshot(id: Long, marketPairWithDetails: MarketPairWithDetails)

    suspend fun createSnapshotWithDetails(list: List<MarketPairWithDetails>)
}