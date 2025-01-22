package com.example.masterproject.sources.marketpairs

import android.util.Log
import com.example.masterproject.model.marketpair.SymbolPriceSource
import com.example.masterproject.sources.base.BaseRetrofitSource
import com.example.masterproject.sources.base.RetrofitConfig
import com.example.masterproject.ui.tools.formatUnixTimeMillis
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitSymbolPriceSource @Inject constructor(
    config: RetrofitConfig
): BaseRetrofitSource(config), SymbolPriceSource {
    val symbolPriceApi = retrofit.create(SymbolPriceApi::class.java)

    override suspend fun getSymbolPrice(listSymbols: List<String>): Map<String, Double> {
        val listPriceResponseEntity = symbolPriceApi.getMarketPrice()
        val symbolAndPrices = mutableMapOf<String, Double>()

        for (symbol in listSymbols){
            symbolAndPrices[symbol] = listPriceResponseEntity.get(listPriceResponseEntity.indexOfFirst { it.symbol == symbol }).price
        }
        return symbolAndPrices
    }


    override suspend fun getPriceAtTheStartDay(symbol: String): Map<String, Double> {
        val arraySymbolInformation =
            symbolPriceApi.getInfoForSymbolForDay(symbol)
//        val price = listSymbolInformation[0].open.toDouble()
        val price = arraySymbolInformation[0][1].toString().toDouble()

        println("symbol123 = $symbol + price = $price")
        return mapOf(symbol to price)
    }

}

