package com.example.masterproject.model.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.masterproject.model.marketpair.database.MarketPairDao
import com.example.masterproject.model.marketpair.database.entities.MarketPairDbEntity
import com.example.masterproject.model.marketsnapshot.database.MarketSnapshotDao
import com.example.masterproject.model.marketsnapshot.database.entities.MarketSnapshotDbEntity
import com.example.masterproject.model.marketsnapshot.database.entities.MarketSnapshotDetailsDbEntity


@Database(entities = [
    MarketPairDbEntity::class,
    MarketSnapshotDbEntity::class,
    MarketSnapshotDetailsDbEntity::class],
    version = 2,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2)
    ])
abstract class AppDatabase : RoomDatabase() {
    abstract fun marketPairDao(): MarketPairDao

    abstract fun marketSnapshotDao(): MarketSnapshotDao
}

object DatabaseConfig {
    const val DATABASE_NAME = "db"
}
