package com.example.financeapptestversion.screens.home

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.financeapptestversion.components.FinanceAppBar
import com.example.financeapptestversion.model.Transaction
import com.example.financeapptestversion.navigation.AppScreens

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Home(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()) {

    val keyboardController = LocalSoftwareKeyboardController.current

    var expanded by remember { mutableStateOf(false) }

    val openDialog = remember {
        mutableStateOf(false)
    }

    val openAddDialog = remember {
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
                openAddDialog.value = true
//                viewModel.addTransaction(
//                    Transaction(amount = 2315.2, title = "TestingDb")
//                )
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


                if (openAddDialog.value) {

                    var transactionAmount = remember {
                        mutableStateOf(0.0)
                    }

                    var transactionAmountText = remember {
                        mutableStateOf(transactionAmount.value.toString())
                    }

                    var transactionName = remember {
                        mutableStateOf("")
                    }

                    var transactionIsExpense = remember {
                        mutableStateOf(false)
                    }

                    var context = LocalContext.current

                    ShowDialog(title = "Add transaction", openDialog = openAddDialog, content = {
                        Column {
                            Text(text = "Add a new transaction")
                            OutlinedTextField(
                                value = transactionName.value,
                                onValueChange = { input ->
                                    transactionName.value = input
                                },
                                label = { Text("Enter the transactions name") },
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

                            Row(
                                Modifier
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.LightGray)
                            ) {
                                val selectedColor = Color.Blue
                                val unselectedColor = Color.Gray

                                Text(
                                    "Income",
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { transactionIsExpense.value = false }
                                        .background(if (!transactionIsExpense.value) Color.Green else unselectedColor)
                                        .padding(8.dp),
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    "Expense",
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { transactionIsExpense.value = true }
                                        .background(if (transactionIsExpense.value) Color.Red else unselectedColor)
                                        .padding(8.dp),
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Text(text = "Add amount")
                            OutlinedTextField(
                                value = transactionAmountText.value.toString(),
                                onValueChange = { input ->
                                    if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                        transactionAmountText.value = input
                                        transactionAmount.value = input.toDoubleOrNull() ?: 0.0
                                    }
                                },
                                label = { Text("Enter amount") },
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
                        }
                    }, onYesPressed = {

                        if (transactionName.value.isEmpty() || transactionAmount.value.toString()
                                .isEmpty()
                        ) {
                            Toast.makeText(
                                context,
                                "Please enter the transaction's name and amount.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModel.addTransaction(
                                Transaction(
                                    title = transactionName.value,
                                    amount = transactionAmount.value,
                                    isExpense = transactionIsExpense.value
                                )
                            )
                            openAddDialog.value = false
                        }
                    })


                }

                if (openDialog.value) {
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

//                Text(
//                    text = "Spending Overview",
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.SemiBold
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))

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
                navController.navigate(AppScreens.StatsScreen.name)
            })
            DropdownMenuItem(text = { Text("Settings") }, onClick = { expanded = false })
            DropdownMenuItem(text = { Text("Stocks") }, onClick = {
                expanded = false
                navController.navigate(AppScreens.StocksScreen.name)
            })
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
    Card(
        modifier = Modifier
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
                color = if (transaction.isExpense) Color(0xFFF44336) else Color(0xFF4CAF50)
            )
        }
    }
}