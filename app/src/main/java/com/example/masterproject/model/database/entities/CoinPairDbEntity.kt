package com.example.masterproject.model.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.masterproject.model.entities.PairCoin

@Entity(tableName = "coin_pairs")
    data class CoinPairDbEntity(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val pair_coin: String
    ){
        fun toPairCoin(): PairCoin = PairCoin(
            id = id,
            pair = pair_coin,
            sourceName = "Binance"
        )

         companion object{
             fun fromCoinPair(pairCoin: PairCoin) = CoinPairDbEntity(
                 id = pairCoin.id,
                 pair_coin = pairCoin.pair,
             )
         }
    }
