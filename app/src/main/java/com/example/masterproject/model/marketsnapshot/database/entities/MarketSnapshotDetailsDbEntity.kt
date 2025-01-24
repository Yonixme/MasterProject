package com.example.masterproject.model.marketsnapshot.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "market_pairs_snapshot_details",
    indices =[
        Index(value = ["snapshot_id", "trade_pair", "source_name"], unique = true)
    ],
    primaryKeys = ["snapshot_id", "trade_pair", "source_name"],
    foreignKeys = [
        ForeignKey(
            entity = MarketSnapshotDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["snapshot_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE)
    ])
data class MarketSnapshotDetailsDbEntity(
    @ColumnInfo(name = "snapshot_id") val snapshotId: Long,
    @ColumnInfo(name = "trade_pair", collate = ColumnInfo.NOCASE) val tradePair: String,
    @ColumnInfo(name = "source_name", collate = ColumnInfo.NOCASE) val sourceName: String,
    val price: Double
    )