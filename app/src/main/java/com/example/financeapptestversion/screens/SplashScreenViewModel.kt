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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
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

    val cloudAccount: MutableState<DataOrException<AccountCashBalance, Boolean, Exception>> =
        mutableStateOf(
            DataOrException(
                AccountCashBalance(),
                true,
                Exception("")
            )
        )


    val localAccount: MutableState<AccountCashBalance> = mutableStateOf(AccountCashBalance())
    val localAccountCashBalance: MutableState<Double> = mutableStateOf(0.0)
    var localAccountTransactions: MutableState<List<Transaction>> = mutableStateOf(listOf())

    //val cloudAccount: MutableState<AccountCashBalance> = mutableStateOf(AccountCashBalance())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllTransactionsFromDatabase()
        }
        viewModelScope.launch(Dispatchers.IO) {
            getAccountCashBalance()
        }
        viewModelScope.launch(Dispatchers.IO) {
            getLocalTransactions()
        }
        viewModelScope.launch(Dispatchers.IO) {
            getLocalAccountCashBalanceII()
        }
        viewModelScope.launch(Dispatchers.IO) {
            getLocalAccount()
        }
    }

    fun getLocalTransactions() {
        viewModelScope.launch {
            accountTransactionsRepository.getAllTransactions().collect { transactions ->
                localAccountTransactions.value = transactions
            }
        }
    }

    fun getLocalAccount() {
        viewModelScope.launch {
            accountTransactionsRepository.getAccount().collect {
                localAccount.value = it
            }
        }
    }

    fun getLocalAccountCashBalance() {
        viewModelScope.launch {
            localAccountCashBalance.value = accountTransactionsRepository.getAccountBalance() ?: 0.0
        }
    }

    fun getLocalAccountCashBalanceII() {
        viewModelScope.launch {
            localAccountCashBalance.value = accountTransactionsRepository.getAccount().firstOrNull()?.balance ?: 0.0
        }
    }

    fun getAllTransactionsFromDatabase() {
        viewModelScope.launch {
            accountTransactions.value.loading = true
            accountTransactions.value = repository.getAllTransactionsFromDatabase()
            if (accountTransactions.value.data != null) accountTransactions.value.loading = false
        }
    }

    fun getAccountCashBalance() {
        viewModelScope.launch {
            cloudAccount.value.loading = true
            cloudAccount.value = repository.getAccountFromDatabase()
            if (cloudAccount.value.data != null) cloudAccount.value.loading = false
        }
    }


    fun addTransactions(transactions: List<Transaction>) {
        viewModelScope.launch {
            transactions.forEach { transaction ->
                accountTransactionsRepository.addTransaction(transaction)
            }
        }
    }

    fun addAccountCashBalance(accountCashBalance: AccountCashBalance) {
        viewModelScope.launch {
            accountTransactionsRepository.updateCashBalance(accountCashBalance.balance)
        }
    }

}