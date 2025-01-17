package com.example.masterproject.model.marketpair.database

import com.example.masterproject.model.modules.IoDispatcher
import com.example.masterproject.model.marketpair.database.entities.MarketPairDbEntity
import com.example.masterproject.model.marketpair.entities.MarketPair
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomMarketPairRepository @Inject constructor(
    private val marketPairDao: MarketPairDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DBMarketPairRepository
{
    override fun getAllMarketPairs(sourceName: String): Flow<List<MarketPairDbEntity?>> = marketPairDao.getAllMarketPairs()

    override suspend fun deleteMarketPair(id: Long) {
        marketPairDao.deletePairById(id)
    }

    override suspend fun AddMarketPair(marketPair: MarketPair) {
        val addItem = MarketPairDbEntity.fromMarketPair(marketPair)

        marketPairDao.insertPair(addItem)
    }
}