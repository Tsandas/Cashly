package com.example.financeapptestversion.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.financeapptestversion.model.AccountCashBalance
import com.example.financeapptestversion.model.Transaction
import com.example.financeapptestversion.utils.DateConverter
import com.example.financeapptestversion.utils.TimestampConverter

@Database(entities = [Transaction::class, AccountCashBalance::class], version = 19, exportSchema = false)
@TypeConverters(DateConverter::class, TimestampConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountCashBalanceDao(): AccountCashBalanceDao
}