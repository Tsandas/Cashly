package com.example.financeapptestversion.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

@Entity(tableName = "account_cash_balance")
data class AccountCashBalance(
    @PrimaryKey
    val id: Int = 0,

    @ColumnInfo(name = "cash_balance")
    val balance: Double = 0.0,

    @ColumnInfo(name = "firebase_user_id")
    var firebaseUserId: String = "",

    @ColumnInfo(name = "last_activity_timestamp")
    var lastActivityTimestamp: Timestamp = Timestamp.now()
)
