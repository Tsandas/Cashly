package com.example.financeapptestversion.screens.stocks

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapptestversion.data.DataOrException
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockScreenViewModel @Inject constructor(
    private val repository: FireRepository
) : ViewModel() {

    val data: MutableState<DataOrException<List<MStockItem>, Boolean, Exception>> = mutableStateOf(
        DataOrException(listOf(), true, Exception(""))
    )


//    val tempHotStocks = listOf<String>(
//        "AMZN", "MSFT", "NVDA", "GOOGL", "META"
//    )
//
//    val hotStocks: MutableState<DataOrException<List<MStockItem>, Boolean, Exception>> = mutableStateOf(
//        DataOrException(listOf(), true, Exception(""))
//    )
//
//    fun getHotStocks(): List<MStockItem> {
//        val hotStocks = mutableListOf<MStockItem>()
//        for (symbol in tempHotStocks) {
//            viewModelScope.launch {
//                val stock = repository.getStock(symbol).data
//                if (stock != null) {
//                    hotStocks.add(stock)
//                }
//            }
//        }
//        Log.d("StockScreenViewModel", "getHotStocks(): ${hotStocks}")
//        return hotStocks
//    }

    val stocksState = mutableStateOf<DataOrException<List<MStockItem>, Boolean, Exception>>(
        DataOrException(listOf(), true, Exception(""))
    )

    fun getAllStocksFromDatabase() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllStocksFromDatabase()
            if (!data.value.data.isNullOrEmpty()) data.value.loading = false
        }
        Log.d(
            "StockScreenViewModel",
            "getAllStocksFromDatabase(): ${data.value.data?.toList().toString()}"
        )
    }

    init {
        loadStocksWithLatestPrices()
        getAllStocksFromDatabase()
    }


    fun loadStocksWithLatestPrices() {
        viewModelScope.launch {
            //stocksState.value = stocksState.value.copy(loading = true)
            val localStocks = repository.getAllStocksFromDatabase()

            val updatedStocks = localStocks.data?.map { stock ->
                try {
                    val latestData = repository.getStock(stock.symbol).data
                    latestData?.let { updated ->
                        if (stock.price != updated.price) {
                            repository.updateStockPrice(stock.id.toString(), updated.price)
                            stock.copy(price = updated.price)
                        } else {
                            stock
                        }
                    } ?: stock
                } catch (e: Exception) {
                    Log.e("StockScreenViewModel", "loadStocksWithLatestPrices(): ${e.message}")
                    stock
                }
            }

            stocksState.value = DataOrException(
                data = updatedStocks,
                loading = false,
                e = localStocks.e
            )
            Log.d(
                "StockScreenViewModel",
                "loadStocksWithLatestPrices(): ${stocksState.value.data?.toList().toString()}"
            )
        }
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

            if (stock.value.data.toString().isNotEmpty()) stock.value.loading = false

        }
        return stock
    }

}