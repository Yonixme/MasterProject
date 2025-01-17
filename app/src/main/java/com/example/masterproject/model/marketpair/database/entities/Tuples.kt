package com.example.masterproject.model.marketpair.database.entities

import androidx.room.ColumnInfo

data class MarketPairTuple(
    val id: Long,
    @ColumnInfo(name = "trade_pair") val tradePair: String,
)

