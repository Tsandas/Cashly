package com.example.financeapptestversion.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.financeapptestversion.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transaction_table")
    fun getTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transaction_table WHERE id = :id")
    suspend fun getTransactionById(id: String): Transaction

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(transaction: Transaction)

    @Query("DELETE FROM transaction_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)


}