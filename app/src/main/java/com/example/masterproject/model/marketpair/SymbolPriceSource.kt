package com.example.masterproject.model.marketpair

import javax.inject.Singleton


interface SymbolPriceSource {

    suspend fun getSymbolPrice(listSymbols: List<String>) : Map<String, Double>

    suspend fun getPriceAtTheStartDay(symbol: String) : Map<String, Double>
}