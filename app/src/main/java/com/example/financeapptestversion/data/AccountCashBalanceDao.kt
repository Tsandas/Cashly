package com.example.financeapptestversion.data

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.financeapptestversion.model.AccountCashBalance

@Dao
interface AccountCashBalanceDao {

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(account: AccountCashBalance)

}