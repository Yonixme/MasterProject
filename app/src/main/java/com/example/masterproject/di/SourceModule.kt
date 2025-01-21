package com.example.masterproject.di

import com.example.masterproject.model.marketpair.SymbolPriceSource
import com.example.masterproject.sources.marketpairs.RetrofitSymbolPriceSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SourceModule{

    @Binds
    abstract fun bindSymbolPriceSource(retrofitSymbolPriceSource: RetrofitSymbolPriceSource): SymbolPriceSource
}