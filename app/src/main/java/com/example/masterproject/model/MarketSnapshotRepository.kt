package com.example.masterproject.model

import com.example.masterproject.model.database.DBRepositories
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
    private val dbRepositories: DBRepositories
) {
    private val customScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _listMarketSnapshotDetails: MutableStateFlow<List<MarketSnapshotAndDetails>> = MutableStateFlow(
        emptyList()
    )

    init {
        customScope.launch {
            queryMarketSnapshotDetails().collect{
                _listMarketSnapshotDetails.value = it
            }
        }
    }

    fun geListMarketSnapshotDetails(): Flow<List<MarketSnapshotAndDetails>> = _listMarketSnapshotDetails


    private fun queryMarketSnapshotDetails(): Flow<List<MarketSnapshotAndDetails>> {
        return dbRepositories.roomMarketSnapshotRepository.getMarketSnapshotAndDetails()

    }
}