package com.example.financeapptestversion.screens.stats

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapptestversion.data.DataOrException
import com.example.financeapptestversion.model.AccountCashBalance
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.repository.AccountTransactionsRepository
import com.example.financeapptestversion.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinanceStatsScreenViewModel @Inject constructor(
    private val fireRepository: FireRepository,
    private val accountTransactionsRepository: AccountTransactionsRepository
) : ViewModel() {

    val data: MutableState<DataOrException<List<MStockItem>, Boolean, Exception>> = mutableStateOf(
        DataOrException(listOf(), true, Exception(""))
    )
    val stocksState = mutableStateOf<DataOrException<List<MStockItem>, Boolean, Exception>>(
        DataOrException(listOf(), true, Exception(""))
    )
    val localAccount: MutableState<AccountCashBalance> = mutableStateOf(AccountCashBalance())

    fun getAllStocksFromDatabase() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = fireRepository.getAllStocksFromDatabase()
            if (!data.value.data.isNullOrEmpty()) data.value.loading = false
        }
    }

    init {
        loadStocksWithLatestPrices()
        getAllStocksFromDatabase()
        getLocalAccount()
    }

    fun getLocalAccount() {
        viewModelScope.launch {
            accountTransactionsRepository.getAccount().collect {
                localAccount.value = it
            }
        }
    }

    fun loadStocksWithLatestPrices() {
        viewModelScope.launch {
            val localStocks = fireRepository.getAllStocksFromDatabase()

            val updatedStocks = localStocks.data?.map { stock ->
                try {
                    val latestData = fireRepository.getStock(stock.symbol).data
                    latestData?.let { updated ->
                        if (stock.price != updated.price) {
                            fireRepository.updateStockPrice(stock.id.toString(), updated.price)
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


}