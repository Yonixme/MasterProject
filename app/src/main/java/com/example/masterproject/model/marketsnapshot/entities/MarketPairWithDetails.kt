package com.example.masterproject.model.marketsnapshot.entities

data class MarketSnapshot(
    val id: Long,
    val time: Long,
)

data class MarketSnapshotAndDetails(
    val marketSnapshot: MarketSnapshot,
    val tradePairs: List<String>,
    val sourceNames: List<String>,
    val prices: List<Double>
)
