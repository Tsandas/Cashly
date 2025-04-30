package com.example.financeapptestversion.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.financeapptestversion.model.AccountCashBalance
import com.example.financeapptestversion.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.forEach


fun isConnectedToWifi(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = cm.activeNetwork ?: return false
    val capabilities = cm.getNetworkCapabilities(network) ?: return false
    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
}

fun syncDataToFireBase(
    context: Context,
    transactions: List<Transaction>,
    account: AccountCashBalance,
) {

    if (!isConnectedToWifi(context)) {
        Log.d("FirebaseSync", "No Wi-Fi connection")
        return
    }

    val db = FirebaseFirestore.getInstance()

    val accountCollection = db.collection("accounts")

    if(account.firebaseUserId.isEmpty()) {
        account.firebaseUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
    }

    accountCollection.document(account.firebaseUserId)
        .set(account)
        .addOnSuccessListener { Log.d("FirebaseSync", "Account synced") }
        .addOnFailureListener {Log.e("FirebaseSync", "Failed to sync account", it) }

    val transactionCollection = db.collection("transactions")
    transactions.forEach { transaction ->
        transactionCollection
            .document(transaction.id.toString())
            .set(transaction)
            .addOnSuccessListener { Log.d("FirebaseSync", "Transaction ${transaction.id} synced") }
            .addOnFailureListener {
                Log.e(
                    "FirebaseSync",
                    "Failed to sync transaction ${transaction.id}",
                    it
                )
            }
    }

}