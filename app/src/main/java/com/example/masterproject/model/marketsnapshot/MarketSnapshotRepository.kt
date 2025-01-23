package com.example.masterproject.model.marketsnapshot

import com.example.masterproject.model.marketpair.database.DBMarketPairRepository
import com.example.masterproject.model.marketpair.entities.MarketPairWithDetails
import com.example.masterproject.model.marketsnapshot.database.DBMarketSnapshotRepository
import com.example.masterproject.model.marketsnapshot.entities.MarketSnapshotAndDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketSnapshotRepository @Inject constructor(
    private val dbMarketSnapshotRepository: DBMarketSnapshotRepository
) {
    private val customScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _listMarketSnapshotDetails: MutableStateFlow<List<MarketSnapshotAndDetails>?> = MutableStateFlow(null)

    init {
        customScope.launch {
            queryMarketSnapshotDetails().collect{
                _listMarketSnapshotDetails.value = it
            }
        }
    }

    suspend fun clearTable(){
        dbMarketSnapshotRepository.clearTable()
    }

    suspend fun saveMarketSnapshot(list: List<MarketPairWithDetails>?){
        if(list.isNullOrEmpty()) return
        dbMarketSnapshotRepository.createSnapshotWithDetails(list = list)
    }

    fun geListMarketSnapshotDetails(): Flow<List<MarketSnapshotAndDetails>?> = _listMarketSnapshotDetails

    suspend fun deleteSnapshotById(id: Long){
        val listSnapshotWithDetails = _listMarketSnapshotDetails.value ?: emptyList()
        val indexOfElement = listSnapshotWithDetails.indexOfFirst { it.marketSnapshot.id == id }
        if (indexOfElement < 0) return

        dbMarketSnapshotRepository.deleteSnapshotById(id)
    }

    private fun queryMarketSnapshotDetails(): Flow<List<MarketSnapshotAndDetails>> {
        return dbMarketSnapshotRepository.getMarketSnapshotAndDetails()
    }
}