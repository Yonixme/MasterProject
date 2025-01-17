package com.example.masterproject.model.marketsnapshot.entities

data class MarketSnapshot(
    val id: Long,
    val time: Long,
)

data class MarketPairsWithDetailsSnapshot(
    val id: Long,
    val time: Long,
    val tradePairs: List<String>,
    val prices: List<Double>,
    val sourceName: List<String>
)



data class MarketSnapshotAndDetailsNew(
    val marketSnapshot: MarketSnapshot,
    val tradePairs: List<String>,
    val sourceNames: List<String>,
    val prices: List<Double>
)



//data class MarketSnapshotDetails(
//    val snapshotID: Long,
//    val tradePair: String,
//    val sourceName: String,
//    val price: Double
//)


