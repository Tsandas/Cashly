package com.example.financeapptestversion.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapptestversion.data.DataOrException
import com.example.financeapptestversion.model.Transaction
import com.example.financeapptestversion.repository.AccountTransactionsRepository
import com.example.financeapptestversion.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val repository: FireRepository,
    private val accountTransactionsRepository: AccountTransactionsRepository
) :
    ViewModel() {

    val data: MutableState<DataOrException<List<Transaction>, Boolean, Exception>> =
        mutableStateOf(
            DataOrException(
                listOf(),
                true,
                Exception("")
            )
        )

    init {
        getAllTransactionsFromDatabase()
    }

    fun getAllTransactionsFromDatabase() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllTransactionsFromDatabase()
            if (!data.value.data.isNullOrEmpty()) data.value.loading = false
        }
    }

    fun addTransactions(transactions: List<Transaction>) {
        viewModelScope.launch {
            transactions.forEach { transaction ->
                accountTransactionsRepository.addTransaction(transaction)
            }
        }
    }

}