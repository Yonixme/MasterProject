package com.example.masterproject.model.marketsnapshot.database

import com.example.masterproject.model.marketpair.entities.MarketPairWithDetails
import com.example.masterproject.model.marketsnapshot.database.entities.MarketSnapshotDbEntity
import com.example.masterproject.model.marketsnapshot.entities.MarketSnapshotAndDetailsNew
import kotlinx.coroutines.flow.Flow

interface DBMarketSnapshotRepository {

    //fun getMarketSnapshotAndDetails(): Flow<Map<MarketSnapshotDbEntity, List<MarketSnapshotDetailsDbEntity>>>
    fun getMarketSnapshotAndDetails(): Flow<List<MarketSnapshotAndDetailsNew>>

    suspend fun getSnapshots(): List<MarketSnapshotDbEntity>

    suspend fun createMarketSnapshotAndDetails(id: Long, marketPairWithDetails: MarketPairWithDetails)
}