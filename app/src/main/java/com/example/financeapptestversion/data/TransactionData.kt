package com.example.financeapptestversion.data

import com.example.financeapptestversion.model.AccountCashBalance
import com.example.financeapptestversion.model.Transaction

class TransactionData {

    fun loadTransactions(): List<Transaction> {
        return listOf(
            Transaction(title = "THIS IS SAMPLE DATA", amount = 25.0),
            Transaction(title = "Gas", amount = -10.0),
            Transaction(title = "Rent", amount = -200.0),
            Transaction(title = "Utilities", amount = -50.0),
            Transaction(title = "Groceries", amount = 25.0),
            Transaction(title = "Gas", amount = -10.0),
            Transaction(title = "Rent", amount = -200.0),
            Transaction(title = "Utilities", amount = -50.0)
        )
    }

    fun getAccountBalance(): AccountCashBalance{
        return AccountCashBalance(balance = 5000.0)
    }

}