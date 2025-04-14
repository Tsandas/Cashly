package com.example.financeapptestversion.repository

import android.util.Log
import com.example.financeapptestversion.data.DataOrException
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.network.StocksApi
import com.example.financeapptestversion.utils.Constants.API_KEY
import javax.inject.Inject

class StockRepository @Inject constructor(private val api: StocksApi) {
    private val dataOrException = DataOrException<List<MStockItem>, Boolean, Exception>()

    suspend fun getStock(symbol: String): DataOrException<List<MStockItem>, Boolean, Exception> {
        //return api.getStocks(symbol, API_KEY)

        try {
            dataOrException.loading = true
            dataOrException.data = api.getStocks(symbol/*, API_KEY*/)
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
        }catch (e: Exception) {
            Log.e("TAG", "ERROR: ${e.message}")
            dataOrException.e = e
        }
        return dataOrException

    }

}