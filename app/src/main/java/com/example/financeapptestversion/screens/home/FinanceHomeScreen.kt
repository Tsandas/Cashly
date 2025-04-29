package com.example.financeapptestversion.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.financeapptestversion.components.FinanceAppBar
import com.example.financeapptestversion.model.Transaction
import com.example.financeapptestversion.navigation.AppScreens
import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Home(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()) {
    var expanded by remember { mutableStateOf(false) }

    val openDialog = remember {
        mutableStateOf(false)
    }

    Scaffold(topBar = {
        FinanceAppBar(
            title = "CASHLY",
            showProfile = false,
            navController = navController,
            icon = Icons.Default.MoreVert
        ) {
            expanded = true
        }
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                viewModel.addTransaction(
                    Transaction(amount = 2315.2, title = "TestingDb")
                )
            }, containerColor = Color(0xFF4CAF50), contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Transaction")
        }
    }) { paddingValues ->
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

                var newBalance = remember {
                    mutableStateOf(0.0)
                }
                val inputText = remember { mutableStateOf(newBalance.value.toString()) }

                if (openDialog.value) {
                    val keyboardController = LocalSoftwareKeyboardController.current
                    ShowDialog(title = "Update Balance", openDialog = openDialog, content = {
                        Text(text = "Enter new balance:")
                        OutlinedTextField(
                            value = inputText.value.toString(),
                            onValueChange = { input ->
                                if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                    inputText.value = input
                                    newBalance.value = input.toDoubleOrNull() ?: 0.0
                                }
                            },
                            label = { Text("Update Balance") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            singleLine = true,
                            enabled = true,
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                })
                        )
                    }, onYesPressed = {
                        viewModel.updateCashBalance(newBalance.value)
                        openDialog.value = false
                    })

                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Total Balance",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            var cash = viewModel.accountCashBalance.collectAsState().value?.balance
                            if (cash.toString().isNullOrEmpty()) {
                                Log.d("Cash", "Home: $cash")
                                cash = 0.0
                            }
                            Text(
                                text = "$${cash.toString()}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Button(
                            onClick = { openDialog.value = true },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Update Balance")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Spending Overview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                var transactions = viewModel.transaction_list.collectAsState().value
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (transactions.isNotEmpty()) {

                        items(transactions) { transaction ->
                            TransactionItem(transaction) {
                                viewModel.removeTransaction(transaction)
                            }
                        }
                    } else {
                        item {
                            Text(text = "No transactions found")
                        }
                    }
                }
            }
        }

        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Profile") }, onClick = {
                expanded = false
                navController.navigate(AppScreens.StocksScreen.name)
            })
            DropdownMenuItem(text = { Text("Settings") }, onClick = { expanded = false })
            DropdownMenuItem(text = { Text("Logout") }, onClick = { expanded = false })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDialog(
    title: String,
    openDialog: MutableState<Boolean>,
    onYesPressed: () -> Unit,
    content: @Composable () -> Unit
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = title) },
            text = { content() },
            confirmButton = {
                TextButton(onClick = { onYesPressed.invoke() }) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(text = "No")
                }
            })
    }
}


@Composable
fun TransactionItem(transaction: Transaction, onTransactionClicked: (Transaction) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onTransactionClicked(transaction)
        }
        .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(transaction.title, fontWeight = FontWeight.Bold)
                Text(
                    transaction.entryDate.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Text(
                text = "$${transaction.amount}",
                fontWeight = FontWeight.Bold,
                color = if (transaction.amount > 0) Color(0xFF4CAF50) else Color(0xFFF44336)
            )
        }
    }
}