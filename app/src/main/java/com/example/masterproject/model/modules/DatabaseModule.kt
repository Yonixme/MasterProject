package com.example.masterproject.model.modules

import android.content.Context
import androidx.room.Room
import com.example.masterproject.model.database.AppDatabase
import com.example.masterproject.model.marketpair.database.MarketPairDao
import com.example.masterproject.model.marketsnapshot.database.MarketSnapshotDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "db1")
            .createFromAsset("basedb.db")
            .build()
    }

    @Provides
    fun provideMarketPairDao(database: AppDatabase): MarketPairDao {
        return database.marketPairDao()
    }

    @Provides
    fun provideMarketSnapshotDao(database: AppDatabase): MarketSnapshotDao{
        return database.marketSnapshotDao()
    }
}