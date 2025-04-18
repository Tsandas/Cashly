package com.example.financeapptestversion.repository

import com.example.financeapptestversion.data.DataOrException
import com.example.financeapptestversion.model.MStockItem
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(
    private val queryStocks: Query
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

}