package com.example.financeapptestversion.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapptestversion.model.AccountCashBalance
import com.example.financeapptestversion.model.Transaction
import com.example.financeapptestversion.repository.AccountTransactionsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: AccountTransactionsRepository) :
    ViewModel() {

    private val _transactionList = MutableStateFlow<List<Transaction>>(emptyList())
    val transaction_list = _transactionList.asStateFlow()

    private val _accountCashBalance =
        MutableStateFlow<AccountCashBalance?>(AccountCashBalance(balance = 0.0))
    val accountCashBalance = _accountCashBalance.asStateFlow()

    init {

        viewModelScope.launch(
            Dispatchers.IO
        ) {
            repository.getAllTransactions().distinctUntilChanged().collect { listOfNotes ->
                Log.d("Cash", "init: $listOfNotes")
                _transactionList.value = listOfNotes
            }
        }
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            Log.d("Cash", "init: before")
            var balance = repository.getAccountBalance()
            Log.d("Cash", "init: $balance")
            if (balance == null) {
                val defaultAccount = AccountCashBalance(id = 0, balance = 0.0, firebaseUserId = FirebaseAuth.getInstance().currentUser?.uid.toString())
                repository.insertCashBalance(defaultAccount) // You need to implement this
                //_accountCashBalance.value = defaultAccount
                balance = 0.0
            } else {
                _accountCashBalance.value = AccountCashBalance(id = 0, balance = balance)
            }
        }
    }

    fun setCashBalance(balance: AccountCashBalance) {
        _accountCashBalance.value = balance
    }

    fun addTransaction(transaction: Transaction) {
        var amount =  if (transaction.isExpense) -transaction.amount else transaction.amount
        var newBalance = _accountCashBalance.value!!.balance + amount
        transaction.firebaseUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        Log.d("Cash", "addTransaction: $transaction")
        viewModelScope.launch { repository.addTransaction(transaction) }
        _accountCashBalance.value = AccountCashBalance(id = 0, balance = newBalance)
        viewModelScope.launch { repository.updateCashBalance(newBalance) }
    }

    fun updateTransaction(transaction: Transaction) =
        viewModelScope.launch { repository.updateTransaction(transaction) }

    fun removeTransaction(transaction: Transaction) =
        viewModelScope.launch { repository.deleteTransaction(transaction) }

    fun updateCashBalance(balance: Double) {
        Log.d("Cash", "updateCashBalance: $balance")
        _accountCashBalance.value = AccountCashBalance(id = 0, balance = balance)
        viewModelScope.launch { repository.updateCashBalance(balance) }
    }

    fun getCashBalance() = viewModelScope.launch { repository.getAccountBalance() ?: 1100.02 }


}