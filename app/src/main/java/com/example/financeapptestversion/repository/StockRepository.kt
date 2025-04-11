package com.example.financeapptestversion.repository

import com.example.financeapptestversion.data.DataOrException
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.network.StocksApi
import com.example.financeapptestversion.utils.Constants.API_KEY
import retrofit2.http.Query
import javax.inject.Inject

class StockRepository @Inject constructor(private val api: StocksApi) {
    private val dataOrException = DataOrException<MStockItem, Boolean, Exception>()

    suspend fun getStock(symbol: String): DataOrException<MStockItem, Boolean, Exception> {
        //return api.getStocks(symbol, API_KEY)

        try {
            dataOrException.loading = true
            dataOrException.data = api.getStocks(symbol, API_KEY)
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false

        }catch (e: Exception) {
            dataOrException.e = e
        }
        return dataOrException

    }

}