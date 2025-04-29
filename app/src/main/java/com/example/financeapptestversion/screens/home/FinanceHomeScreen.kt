package com.example.financeapptestversion.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.financeapptestversion.components.FinanceAppBar
import com.example.financeapptestversion.navigation.AppScreens

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Home(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            FinanceAppBar(
                title = "CASHLY",
                showProfile = false,
                navController = navController,
                icon = Icons.Default.MoreVert
            ) {
                expanded = true
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // TODO: Handle adding a transaction
                },
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF2F2F2))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Welcome back, User!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Total Balance", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "$12,540.75",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Spending Overview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Spending Chart (Simple Bar Chart)
                SpendingChart()

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(sampleTransactions) { transaction ->
                        TransactionItem(transaction)
                    }
                }
            }
        }

        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Profile") },
                onClick = {
                    expanded = false
                    navController.navigate(AppScreens.StocksScreen.name)
                }
            )
            DropdownMenuItem(
                text = { Text("Settings") },
                onClick = { expanded = false }
            )
            DropdownMenuItem(
                text = { Text("Logout") },
                onClick = { expanded = false }
            )
        }
    }
}

// ---------- Components below ----------

@Composable
fun SpendingChart() {
    val expenses = listOf(150f, 300f, 200f, 100f, 250f) // Example values

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        expenses.forEach { expense ->
            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height(expense.dp)
                    .background(Color(0xFF4CAF50), shape = RoundedCornerShape(4.dp))
            )
        }
    }
}

data class Transaction(val title: String, val amount: String, val date: String)

val sampleTransactions = listOf(
    Transaction("Starbucks", "-$5.40", "Today"),
    Transaction("Salary", "+$2,500.00", "Yesterday"),
    Transaction("Netflix", "-$12.99", "2 days ago"),
    Transaction("Groceries", "-$85.60", "3 days ago"),
    Transaction("Gym", "-$40.00", "Last week")
)
@Composable
fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(transaction.title, fontWeight = FontWeight.Bold)
                Text(transaction.date.toString(), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text(
                text = transaction.amount.toString(),
                fontWeight = FontWeight.Bold,
                color = if (transaction.amount > 0.toString()) Color(0xFF4CAF50) else Color(0xFFF44336)
            )
        }
    }
}