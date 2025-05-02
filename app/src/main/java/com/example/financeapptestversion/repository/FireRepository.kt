package com.example.financeapptestversion.repository

import android.util.Log
import com.example.financeapptestversion.data.AppDatabase
import com.example.financeapptestversion.data.DataOrException
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.model.Transaction
import com.example.financeapptestversion.network.StocksApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(
    private val queryStocks: Query,
    private val queryTransactions: Query,
    private val api: StocksApi
) {

    suspend fun getAllStocksFromDatabase(): DataOrException<List<MStockItem>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MStockItem>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = queryStocks.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MStockItem::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) {
                dataOrException.loading = false
            }
        } catch (exception: FirebaseFirestoreException) {
            dataOrException.e = exception
        }

        return dataOrException
    }

    suspend fun getAllTransactionsFromDatabase(): DataOrException<List<Transaction>, Boolean, Exception> {
        val dataOrException = DataOrException<List<Transaction>, Boolean, Exception>()
        try {
            dataOrException.loading = true
            dataOrException.data = queryTransactions.whereEqualTo("firebaseUserId", FirebaseAuth.getInstance().currentUser?.uid)
                .get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(Transaction::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) {
                dataOrException.loading = false
            }
        } catch (exception: FirebaseFirestoreException) {
            dataOrException.e = exception
        }
        Log.d("FireRepoViewModel", "getAllTransactionsFromDatabase: ${dataOrException.data}")
        return dataOrException;
    }



    suspend fun getStock(symbol: String): DataOrException<MStockItem, Boolean, Exception> {
        val dataOrException = DataOrException<MStockItem, Boolean, Exception>()
        try {
            dataOrException.loading = true
            dataOrException.data = api.getStocks(symbol/*, API_KEY*/).get(0)
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
        } catch (e: Exception) {
            Log.e("TAG", "ERROR: ${e.message}")
            dataOrException.e = e
        }
        return dataOrException
    }

    suspend fun updateStockPrice(stockId: String, newPrice: Double) {
        val stockRef = FirebaseFirestore.getInstance().collection("stocks").document(stockId)
        stockRef.update("price", newPrice).await()
    }


}