package com.example.masterproject.model.entities

data class SavedCoinPairs(
    val id: Long,
    val time: Long,
    val pairs: List<String>,
    val price: List<Long>
)

data class CoinPairsSnapshot(
    val id: Long,
    val time: Long,
    val pairs: List<String>,
    val price: List<Long>
)
