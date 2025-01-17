package com.example.masterproject.model.marketsnapshot.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.masterproject.model.marketsnapshot.entities.MarketSnapshot

@Entity(tableName = "market_snapshots",
    indices = [
        //Index("time", unique = true)
    ])
data class MarketSnapshotDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val time: Long
) {
    fun toMarketSnapshot(): MarketSnapshot = MarketSnapshot(
        id = id,
        time = time
    )

    companion object{
        fun createSnapshot(): MarketSnapshotDbEntity = MarketSnapshotDbEntity(
            id = 0,
            time = System.currentTimeMillis()
        )
    }
}