package com.example.masterproject.model.marketsnapshot.database

import com.example.masterproject.model.marketpair.entities.MarketPairWithDetails
import com.example.masterproject.model.marketsnapshot.database.entities.MarketSnapshotDbEntity
import com.example.masterproject.model.marketsnapshot.database.entities.MarketSnapshotDetailsDbEntity
import com.example.masterproject.model.marketsnapshot.entities.MarketSnapshot
import com.example.masterproject.model.marketsnapshot.entities.MarketSnapshotAndDetails
import com.example.masterproject.di.IoDispatcher
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

    override suspend fun getSnapshots(): List<MarketSnapshot> {
        return marketSnapshotDao.getSnapshots()
            .map {
                it.toMarketSnapshot()
            }
    }

    override fun getMarketSnapshotAndDetails(): Flow<List<MarketSnapshotAndDetails>> {
        return marketSnapshotDao.getMarketSnapshotAndDetails()
            .map { entities ->
                entities.map {map ->
                    val marketSnapshotEntity = map.key
                    val listDetailsEntity = map.value
                    MarketSnapshotAndDetails(
                        marketSnapshot = marketSnapshotEntity.toMarketSnapshot(),
                        tradePairs = List(listDetailsEntity.size){listDetailsEntity[it].tradePair },
                        sourceNames = List(listDetailsEntity.size){listDetailsEntity[it].sourceName },
                        prices = List(listDetailsEntity.size){listDetailsEntity[it].price },
                    )
                }
            }
    }

    override suspend fun setDetailsForMarketSnapshot(id : Long, marketPairWithDetails: MarketPairWithDetails) {
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

    override suspend fun createSnapshotWithDetails(list: List<MarketPairWithDetails>) {
        val snapshotId = createSnapshot()
        marketSnapshotDao.setDetailsForMarketSnapshot1(
            List(list.size){
                MarketSnapshotDetailsDbEntity(
                    snapshotId = snapshotId,
                    list[it].tradePair,
                    list[it].sourceName,
                    list[it].price
                )
            }
        )
    }

    private suspend fun createSnapshot(): Long{
        return marketSnapshotDao.createMarketSnapshot(MarketSnapshotDbEntity.createSnapshot())
    }
}