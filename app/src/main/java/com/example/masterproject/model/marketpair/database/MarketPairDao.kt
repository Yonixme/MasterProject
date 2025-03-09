package com.example.masterproject.model.marketpair.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.masterproject.model.marketpair.database.entities.MarketPairDbEntity
import com.example.masterproject.model.marketpair.database.entities.MarketPairTuple
import dagger.Provides
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketPairDao{
    @Insert
    suspend fun insertPair(marketPair: MarketPairDbEntity)

    @Query("SELECT * FROM market_pairs")
    fun getAllMarketPairs(): Flow<List<MarketPairDbEntity?>>

    @Query("Select id, trade_pair From market_pairs Where source_name = :source")
    suspend fun findBySource(source: String): MarketPairTuple?

    @Query("DELETE FROM market_pairs WHERE id = :id")
    suspend fun deletePairById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setIgnoreSavingFlagForMarketPair(marketPair: MarketPairDbEntity)

    @Query("DELETE FROM market_pairs")
    suspend fun clearTable()
}
