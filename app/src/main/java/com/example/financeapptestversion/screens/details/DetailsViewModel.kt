package com.example.financeapptestversion.screens.details

import androidx.lifecycle.ViewModel
import com.example.financeapptestversion.data.DataOrException
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: StockRepository) : ViewModel() {

    suspend fun getStockInfo(stockSymbol: String): DataOrException<MStockItem, Boolean, Exception> {
        return repository.getStock(stockSymbol)
    }

}