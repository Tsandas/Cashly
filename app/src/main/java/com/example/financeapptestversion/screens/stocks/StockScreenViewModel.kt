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

    init {
        getAllStocksFromDatabase()
    }

    fun getAllStocksFromDatabase() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllStocksFromDatabase()
            if (!data.value.data.isNullOrEmpty()) data.value.loading = false
        }
        Log.d("TAG", "getAllStocksFromDatabase: ${data.value.data?.toList().toString()}")
    }

}