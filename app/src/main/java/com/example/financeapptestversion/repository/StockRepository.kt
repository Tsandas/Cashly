package com.example.financeapptestversion.repository

import android.util.Log
import com.example.financeapptestversion.data.DataOrException
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.network.StocksApi
import javax.inject.Inject

class StockRepository @Inject constructor(private val api: StocksApi) {
    suspend fun getStock(symbol: String): DataOrException<MStockItem, Boolean, Exception> {
        val dataOrException = DataOrException<MStockItem, Boolean, Exception>()
        try {
            dataOrException.loading = true
            dataOrException.data = api.getStocks(symbol/*, API_KEY*/).get(0)
            if (dataOrException.data.toString().isNotEmpty()) {
                dataOrException.loading = false
                Log.d("StockRepository", "API REQUEST SUCCESSFUL ${dataOrException.data}")
            }
        } catch (e: Exception) {
            Log.e("StockRepository", "ERROR: ${e.message}")
            dataOrException.e = e
        }
        return dataOrException
    }

    suspend fun getStockHistory(symbol: String): DataOrException<List<MStockItem>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MStockItem>, Boolean, Exception>()
        try {
            dataOrException.loading = true
            dataOrException.data = api.getStocks(symbol/*, API_KEY*/)
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
        } catch (e: Exception) {
            Log.e("TAG", "ERROR: ${e.message}")
            dataOrException.e = e
        }
        return dataOrException
    }

}