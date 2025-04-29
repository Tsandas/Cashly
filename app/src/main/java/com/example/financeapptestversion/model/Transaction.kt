package com.example.financeapptestversion.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.Date
import java.util.UUID

@Entity(tableName = "transaction_table")
data class Transaction(

    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "firebase_user_id")
    val firebaseUserId: String = "",

    @ColumnInfo(name = "transaction_title")
    val title: String,

    @ColumnInfo(name = "note_entry_date")
    val entryDate: Date = Date.from(Instant.now()),

    @ColumnInfo(name = "transaction_amount")
    val amount: Double

)
