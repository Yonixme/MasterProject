package com.example.masterproject.model.marketsnapshot.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.masterproject.model.marketsnapshot.database.entities.MarketSnapshotDbEntity
import com.example.masterproject.model.marketsnapshot.database.entities.MarketSnapshotDetailsDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketSnapshotDao {

    @Insert
    suspend fun setDetailsForMarketSnapshot(marketSnapshotDetailsDbEntity: MarketSnapshotDetailsDbEntity)

    @Query("SELECT * FROM market_snapshots JOIN market_pairs_snapshot_details ON market_snapshots.id = market_pairs_snapshot_details.snapshot_id")
    fun getMarketSnapshotAndDetails() : Flow<Map<MarketSnapshotDbEntity, List<MarketSnapshotDetailsDbEntity>>>

    @Insert
    suspend fun createMarketSnapshot(marketSnapshotDBEntity: MarketSnapshotDbEntity): Long

    @Query("SELECT * FROM market_snapshots")
    fun getSnapshots(): List<MarketSnapshotDbEntity>





//    @Insert
//    suspend fun insertAllDetailsForMarketSnapshot(list: List<MarketSnapshotDetailsDbEntity>)

    //    @Insert
//    suspend fun createMarketSnapshotDetails(marketSnapshotDetailsDbEntity: MarketSnapshotDetailsDbEntity)
//
//    @Query("SELECT * FROM market_snapshots")
//    fun getAllMarketSnapshots(): Flow<List<MarketSnapshotDbEntity?>>

//    @Query("SELECT * FROM market_snapshots WHERE id = :id")
//    fun getMarketSnapshotByID(id: Long): Flow<MarketSnapshotDbEntity?>




}