package com.example.financeapptestversion.repository

import android.util.Log
import com.example.financeapptestversion.data.AppDatabase
import com.example.financeapptestversion.model.AccountCashBalance
import com.example.financeapptestversion.model.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountTransactionsRepository @Inject constructor(private val appDatabase: AppDatabase)  {


    suspend fun addTransaction(transaction: Transaction) = appDatabase.transactionDao().addTransaction(transaction)

    suspend fun updateTransaction(transaction: Transaction) = appDatabase.transactionDao().updateTransaction(transaction)

    suspend fun deleteTransaction(transaction: Transaction) = appDatabase.transactionDao().deleteTransaction(transaction)

    fun getAllTransactions(): Flow<List<Transaction>> = appDatabase.transactionDao().getTransactions()

    suspend fun deleteAllTransactions() = appDatabase.transactionDao().deleteAll()

    suspend fun getTransactionById(id: String): Transaction = appDatabase.transactionDao().getTransactionById(id)


    suspend fun getAccountBalance(): Double? {
        val accountCashBalance = appDatabase.accountCashBalanceDao().getCashBalance()
        return accountCashBalance
    }

    suspend fun updateCashBalance(balance: Double) {
        Log.d("Cash", "updateCashBalance: $balance")
        appDatabase.accountCashBalanceDao().updateCashBalance(balance)
    }
    suspend fun insertCashBalance(accountCashBalance: AccountCashBalance) = appDatabase.accountCashBalanceDao().insertCashBalance(accountCashBalance)
}