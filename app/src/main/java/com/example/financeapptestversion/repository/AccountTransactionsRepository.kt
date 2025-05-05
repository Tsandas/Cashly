package com.example.financeapptestversion.repository

import com.example.financeapptestversion.data.AppDatabase
import com.example.financeapptestversion.model.AccountCashBalance
import com.example.financeapptestversion.model.Transaction
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountTransactionsRepository @Inject constructor(private val appDatabase: AppDatabase) {

    suspend fun addTransaction(transaction: Transaction) {
        updateLastActivityTimestamp()
        appDatabase.transactionDao().addTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        updateLastActivityTimestamp()
        appDatabase.transactionDao().updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        updateLastActivityTimestamp()
        appDatabase.transactionDao().deleteTransaction(transaction)
    }

    fun getAllTransactions(): Flow<List<Transaction>> =
        appDatabase.transactionDao().getTransactions()

    suspend fun deleteAllTransactions() = appDatabase.transactionDao().deleteAll()

    suspend fun getTransactionById(id: String): Transaction =
        appDatabase.transactionDao().getTransactionById(id)


    suspend fun getAccountBalance(): Double? {
        val accountCashBalance = appDatabase.accountCashBalanceDao().getCashBalance()
        return accountCashBalance
    }

    suspend fun updateCashBalance(balance: Double) {
        updateLastActivityTimestamp()
        appDatabase.accountCashBalanceDao().updateCashBalance(balance)
    }

    fun getAccount(): Flow<AccountCashBalance> = appDatabase.accountCashBalanceDao().getAccount()

    suspend fun insertCashBalance(accountCashBalance: AccountCashBalance) =
        appDatabase.accountCashBalanceDao().insertCashBalance(accountCashBalance)

    suspend fun resetCashBalance() = appDatabase.accountCashBalanceDao().resetCashBalance()

    suspend fun updateLastActivityTimestamp() {
        val timestamp = Timestamp.now()
        updateLastActivityTimestamp(timestamp)
    }

    suspend fun updateLastActivityTimestamp(timestamp: Timestamp) =
        appDatabase.accountCashBalanceDao().updateLastActivityTimestamp(timestamp)

}