package com.example.financeapptestversion.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_cash_balance")
data class AccountCashBalance(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "cash_balance")
    val balance: Double = 0.0,

    @ColumnInfo(name = "firebase_user_id")
    val firebaseUserId: String = ""

)
