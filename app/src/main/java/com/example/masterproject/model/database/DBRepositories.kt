package com.example.masterproject.model.database

import android.content.Context
import com.example.masterproject.model.marketpair.database.RoomMarketPairRepository
import com.example.masterproject.model.marketsnapshot.database.RoomMarketSnapshotRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DBRepositories @Inject constructor(
    val roomMarketPairRepository: RoomMarketPairRepository,
    val roomMarketSnapshotRepository: RoomMarketSnapshotRepository
)
