package com.example.financeapptestversion.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.financeapptestversion.model.AccountCashBalance
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountCashBalanceDao {

    @Query("SELECT * FROM account_cash_balance LIMIT 1")
    fun getAccount(): Flow<AccountCashBalance>

    @Query("UPDATE account_cash_balance SET cash_balance = :balance WHERE id = 0")
    suspend fun updateCashBalance(balance: Double)

    @Query("SELECT cash_balance FROM account_cash_balance LIMIT 1")
    suspend fun getCashBalance(): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCashBalance(account: AccountCashBalance)

    @Query("UPDATE account_cash_balance SET cash_balance = 0 WHERE id = 0")
    suspend fun resetCashBalance()

    @Query("UPDATE account_cash_balance SET last_activity_timestamp = :timestamp WHERE id = 0")
    suspend fun updateLastActivityTimestamp(timestamp: Timestamp)

}