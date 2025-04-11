package com.example.financeapptestversion.network

import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.model.StockModel
import com.example.financeapptestversion.utils.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface StocksApi {

    @GET("search-exchange-variants")
    suspend fun getStocks(
        @Query("symbol") symbol: String,
        @Query(API_KEY) apiKey: String
    ): MStockItem

}