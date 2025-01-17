package com.example.masterproject.model.marketsnapshot.database

import com.example.masterproject.model.marketpair.entities.MarketPairWithDetails
import com.example.masterproject.model.marketsnapshot.database.entities.MarketSnapshotDbEntity
import com.example.masterproject.model.marketsnapshot.database.entities.MarketSnapshotDetailsDbEntity
import com.example.masterproject.model.marketsnapshot.entities.MarketSnapshotAndDetailsNew
import com.example.masterproject.model.modules.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomMarketSnapshotRepository @Inject constructor(
    private val marketSnapshotDao: MarketSnapshotDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): DBMarketSnapshotRepository{
    override suspend fun getSnapshots(): List<MarketSnapshotDbEntity> {
        //marketSnapshotDao.createMarketSnapshot(MarketSnapshotDbEntity.createSnapshot())
        val list = marketSnapshotDao.getSnapshots()
        for (snapshot in list){
            println("snapshot123 ////////////////")
            println("snapshot123 = ${snapshot.id}")
            println("snapshot123 = ${snapshot.time}")
            println("snapshot123 ////////////////")
        }
        return list
    }

    override fun getMarketSnapshotAndDetails(): Flow<List<MarketSnapshotAndDetailsNew>> {
        return marketSnapshotDao.getMarketSnapshotAndDetails()
            .map { entities ->
                entities.map {map ->
                    val marketSnapshotEntity = map.key
                    val listDetailsEntity = map.value
                    MarketSnapshotAndDetailsNew(
                        marketSnapshot = marketSnapshotEntity.toMarketSnapshot(),
                        tradePairs = List(listDetailsEntity.size){listDetailsEntity[it].tradePair },
                        sourceNames = List(listDetailsEntity.size){listDetailsEntity[it].sourceName },
                        prices = List(listDetailsEntity.size){listDetailsEntity[it].price },
                    )
                }
            }
    }

    suspend fun createSnapshot(): Long{
        return marketSnapshotDao.createMarketSnapshot(MarketSnapshotDbEntity.createSnapshot())
    }

    override suspend fun createMarketSnapshotAndDetails(id : Long, marketPairWithDetails: MarketPairWithDetails) {
        //val id = marketSnapshotDao.createMarketSnapshot(MarketSnapshotDbEntity.createSnapshot())
        marketSnapshotDao.setDetailsForMarketSnapshot(
            MarketSnapshotDetailsDbEntity(
                id,
                marketPairWithDetails.tradePair,
                marketPairWithDetails.sourceName,
                marketPairWithDetails.price
            )
        )
    }
}