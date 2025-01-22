package com.example.masterproject.model.marketpair.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.masterproject.model.marketpair.entities.MarketPair

@Entity(tableName = "market_pairs",
    indices =[
        Index(value = ["trade_pair", "source_name"], unique = true)
    ])
data class MarketPairDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "trade_pair", collate = ColumnInfo.NOCASE) val tradePair: String,
    @ColumnInfo(name = "source_name", collate = ColumnInfo.NOCASE) val sourceName: String,
    @ColumnInfo(name = "ignore_when_saving") val ignoreWhenSaving: Boolean?
){
    fun toMarketPair(): MarketPair = MarketPair(
        id = id,
        tradePair = tradePair,
        sourceName = sourceName,
        ignoreWhenSaving = ignoreWhenSaving ?: false
    )

    companion object{
        fun fromMarketPair(marketCoin: MarketPair) = MarketPairDbEntity(
            id = marketCoin.id,
            tradePair = marketCoin.tradePair,
            sourceName = marketCoin.sourceName,
            ignoreWhenSaving = marketCoin.ignoreWhenSaving
        )
    }
}
