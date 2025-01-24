package com.example.masterproject.model.marketpair.database

import com.example.masterproject.di.IoDispatcher
import com.example.masterproject.model.marketpair.database.entities.MarketPairDbEntity
import com.example.masterproject.model.marketpair.entities.MarketPair
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomMarketPairRepository @Inject constructor(
    private val marketPairDao: MarketPairDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DBMarketPairRepository
{
    override fun getAllMarketPairs(): Flow<List<MarketPair?>> {
        return marketPairDao.getAllMarketPairs()
            .map {list ->
                list.map {
                    it?.toMarketPair()
                }
            }
    }

    override suspend fun clearTable() {
        marketPairDao.clearTable()
    }

    override suspend fun deleteMarketPair(id: Long) {
        marketPairDao.deletePairById(id)
    }

    override suspend fun addMarketPair(marketPair: MarketPair) {
        val addItem = MarketPairDbEntity.fromMarketPair(marketPair)

        marketPairDao.insertPair(addItem)
    }

    override fun setIgnoreSavingFlagForMarketPair(marketPair: MarketPair) {
        val debug = MarketPairDbEntity.fromMarketPair(marketPair)
        marketPairDao.setIgnoreSavingFlagForMarketPair(MarketPairDbEntity.fromMarketPair(marketPair))
    }
}