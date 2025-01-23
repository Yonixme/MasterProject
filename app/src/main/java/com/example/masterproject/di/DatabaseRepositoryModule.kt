package com.example.masterproject.di

import com.example.masterproject.model.marketpair.database.DBMarketPairRepository
import com.example.masterproject.model.marketpair.database.RoomMarketPairRepository
import com.example.masterproject.model.marketsnapshot.database.DBMarketSnapshotRepository
import com.example.masterproject.model.marketsnapshot.database.RoomMarketSnapshotRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseRepositoryModule{

    @Binds
    abstract fun bindDBMarketPairRepository(roomMarketPairRepository: RoomMarketPairRepository) : DBMarketPairRepository

    @Binds
    abstract fun bindDBMarketSnapshotRepository(roomMarketSnapshotRepository: RoomMarketSnapshotRepository): DBMarketSnapshotRepository
}