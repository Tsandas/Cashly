package com.example.financeapptestversion.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapptestversion.data.DataOrException
import com.example.financeapptestversion.model.AccountCashBalance
import com.example.financeapptestversion.model.Transaction
import com.example.financeapptestversion.repository.AccountTransactionsRepository
import com.example.financeapptestversion.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val repository: FireRepository,
    private val accountTransactionsRepository: AccountTransactionsRepository
) :
    ViewModel() {

    val accountTransactions: MutableState<DataOrException<List<Transaction>, Boolean, Exception>> =
        mutableStateOf(
            DataOrException(
                listOf(),
                true,
                Exception("")
            )
        )

    val accountCashBalance: MutableState<DataOrException<AccountCashBalance, Boolean, Exception>> =
        mutableStateOf(
            DataOrException(
                AccountCashBalance(),
                true,
                Exception("")
            )
        )

    val localAccountCashBalance: MutableState<Double> = mutableStateOf(0.0)
    var localAccountTransactions: MutableState<List<Transaction>> = mutableStateOf(listOf())

    init {
        getAllTransactionsFromDatabase()
        getAccountCashBalance()
    }

    fun getLocalTransactions(){
        viewModelScope.launch {
            accountTransactionsRepository.getAllTransactions().collect { transactions ->
                localAccountTransactions.value = transactions
            }
        }
    }

    fun getLocalAccountCashBalance(){
        viewModelScope.launch {
            localAccountCashBalance.value = accountTransactionsRepository.getAccountBalance()!!
        }
    }

    fun getAllTransactionsFromDatabase() {
        viewModelScope.launch {
            accountTransactions.value.loading = true
            accountTransactions.value = repository.getAllTransactionsFromDatabase()
            if (!accountTransactions.value.data.isNullOrEmpty()) accountTransactions.value.loading = false
        }
    }

    fun getAccountCashBalance(){
        viewModelScope.launch {
            accountCashBalance.value.loading = true
            accountCashBalance.value = repository.getAccountFromDatabase()
            if(accountCashBalance.value.data != null) accountCashBalance.value.loading = false
        }
    }

    fun addTransactions(transactions: List<Transaction>) {
        viewModelScope.launch {
            transactions.forEach { transaction ->
                accountTransactionsRepository.addTransaction(transaction)
            }
        }
    }

    fun addAccountCashBalance(accountCashBalance: AccountCashBalance){
        viewModelScope.launch {
            accountTransactionsRepository.updateCashBalance(accountCashBalance.balance)
        }

    }

}