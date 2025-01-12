package com.example.masterproject.model.database

data class SavedCoinPairs(
    val id: Long,
    val time: Long,
    val pairs: List<String>,
    val price: List<Long>
)
