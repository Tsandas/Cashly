package com.example.financeapptestversion.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.financeapptestversion.model.AccountCashBalance
import com.example.financeapptestversion.model.Transaction
import com.example.financeapptestversion.utils.DateConverter

@Database(entities = [Transaction::class, AccountCashBalance::class], version = 11, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountCashBalanceDao(): AccountCashBalanceDao
}