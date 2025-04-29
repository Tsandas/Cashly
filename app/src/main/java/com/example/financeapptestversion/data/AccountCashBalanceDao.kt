package com.example.financeapptestversion.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.financeapptestversion.model.AccountCashBalance

@Dao
interface AccountCashBalanceDao {

    @Query("UPDATE account_cash_balance SET cash_balance = :balance WHERE id = 0")
    suspend fun updateCashBalance(balance: Double)

    @Query("SELECT cash_balance FROM account_cash_balance LIMIT 1")
    suspend fun getCashBalance(): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCashBalance(account: AccountCashBalance)

}