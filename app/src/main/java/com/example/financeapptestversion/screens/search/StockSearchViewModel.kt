package com.example.financeapptestversion.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.financeapptestversion.data.DataOrException
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.repository.StockRepository
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
class StockSearchViewModel @Inject constructor(private val repository: StockRepository) :
    ViewModel() {

    //Symbol Limited to AAPL, TSLA, AMZN, MSFT, NVDA, GOOGL, META, NFLX, JPM, V, BAC, AMD, PYPL, DIS, T, PFE, COST,
    // INTC, KO, TGT, NKE, SPY, BA, BABA, XOM, WMT, GE, CSCO, VZ, JNJ, CVX, PLTR, SQ, SHOP, SBUX, SOFI, HOOD, RBLX,
    // SNAP, AMD, UBER, FDX, ABBV, ETSY, MRNA, LMT, GM, F, RIVN, LCID, CCL, DAL, UAL, AAL, TSM, SONY, ET, NOK, MRO,
    // COIN, RIVN, SIRI, SOFI, RIOT, CPRX, PYPL, TGT, VWO, SPYG

    val stocksInitList: MutableList<MutableState<DataOrException<MStockItem, Boolean, Exception>>> =
        mutableStateListOf()
    var isLoading: Boolean by mutableStateOf(true)

    init {
        loadStocks()
    }

    private fun loadStocks() {
        viewModelScope.launch {
            isLoading = true
            var initStocks = listOf(
                "AAPL", "TSLA", "NFLX"
            )
            initStocks?.let { list ->
                for (i in list) {
                    val stockItem = searchStock(i)
                    stockItem?.let {
                        stocksInitList.add(it)
                    }
                }

            } ?: Log.e("StockViewModel", "initStocks is null!")
        }
        isLoading = false
    }

    fun searchStock(query: String): MutableState<DataOrException<MStockItem, Boolean, Exception>> {
        val stock: MutableState<DataOrException<MStockItem, Boolean, Exception>> =
            mutableStateOf(DataOrException(null, true, Exception("")))

        viewModelScope.launch(/*Dispatchers.IO*/) {

            if (query.isEmpty()) {
                return@launch
            }

            stock.value.loading = true
            stock.value = repository.getStock(query)

            Log.d("TAG", "searchStocks: ${query} ${stock.value.data.toString()}")

            if (stock.value.data.toString().isNotEmpty()) stock.value.loading = false

        }
        return stock
    }

    fun searchStockII(query: String) {
        val stock: MutableState<DataOrException<MStockItem, Boolean, Exception>> =
            mutableStateOf(DataOrException(null, true, Exception("")))

        viewModelScope.launch(/*Dispatchers.IO*/) {

            if (query.isEmpty()) {
                return@launch
            }

            stock.value.loading = true
            stock.value = repository.getStock(query)

            Log.d("TAG", "searchStocks: ${query} ${stock.value.data.toString()}")

            if (stock.value.data.toString().isNotEmpty()) stock.value.loading = false

        }
        stocksInitList.clear()
        stocksInitList.add(stock)
    }


}