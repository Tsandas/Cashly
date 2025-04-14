package com.example.financeapptestversion.network

import com.example.financeapptestversion.model.MStockItem
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface StocksApi {

    @GET("historical-price-eod/light")
    suspend fun getStocks(
        @Query("symbol") symbol: String,
        //@Query("apikey") apiKey: String = Constants.API_KEY
    ): List<MStockItem>

}