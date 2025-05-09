package com.example.financeapptestversion.screens.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.financeapptestversion.components.BottomBar
import com.example.financeapptestversion.components.FinanceAppBar
import com.example.financeapptestversion.components.ShowDialogHomeScreen
import com.example.financeapptestversion.components.TransactionItem
import com.example.financeapptestversion.model.Transaction
import com.example.financeapptestversion.navigation.AppScreens
import com.example.financeapptestversion.screens.update.ShowAlertDialog
import com.example.financeapptestversion.ui.theme.GreenPrimary
import com.example.financeapptestversion.ui.theme.WarningRed
import com.example.financeapptestversion.utils.syncDataToFireBase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Home(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val openDialog = remember {
        mutableStateOf(false)
    }

    val openAddTransactionDialog = remember {
        mutableStateOf(false)
    }

    val openDeleteTransactionDialog = remember {
        mutableStateOf(false)
    }
    val deleteTransaction = remember {
        mutableStateOf(Transaction())
    }

    val openUpdateTransactionDialog = remember {
        mutableStateOf(false)
    }
    val updateTransaction = remember {
        mutableStateOf(Transaction())
    }

    val syncData = remember {
        mutableStateOf(false)
    }

    val logout = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState, drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxHeight(),
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Spacer(modifier = Modifier.height(32.dp))


                    Text(
                        text = "Cashly", style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    DrawerItem(
                        label = "Profile", icon = Icons.Default.Person, onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(AppScreens.StatsScreen.name)
                        })

                    DrawerItem(
                        label = "Stocks", icon = Icons.Default.ShowChart, onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(AppScreens.StocksScreen.name)
                        })

//                    DrawerItem(
//                        label = "Settings", icon = Icons.Default.Settings, onClick = {
//                            scope.launch { drawerState.close() }
//                            //navController.navigate(AppScreens.SettingsScreen.name)
//                        })

                    DrawerItem(
                        label = "About", icon = Icons.Default.Info, onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(AppScreens.AboutScreen.name)
                        })

                    DrawerItem(
                        label = "Logout", icon = Icons.Default.Logout, onClick = {
                            scope.launch { drawerState.close() }
                            syncData.value = true
                            logout.value = true
                        }, iconTint = WarningRed
                    )

                }
            }
        }) {
        Scaffold(topBar = {
            FinanceAppBar(
                title = "Cashly",
                navController = navController,
                icon = Icons.Default.Menu,
                onIconClicked = {
                    scope.launch { drawerState.open() }
                },
                infoIconAction = { navController.navigate(AppScreens.AboutScreen.name) })
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    openAddTransactionDialog.value = true
                }, containerColor = Color(0xFF4CAF50), contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }, bottomBar = {
            BottomBar(navController, homeScreen = true)
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

                    if (openAddTransactionDialog.value) {
                        AddingTransactionProcess(
                            openAddTransactionDialog, keyboardController, viewModel, syncData
                        )
                    }
                    if (openDialog.value) {
                        UpdatingBalanceProcess(
                            openDialog, keyboardController, viewModel, syncData
                        )
                    }
                    if (openDeleteTransactionDialog.value) {
                        DeleteTransaction(
                            deleteTranscation = openDeleteTransactionDialog,
                            viewModel = viewModel,
                            transaction = deleteTransaction.value,
                            syncData = syncData
                        )
                    }
                    if (openUpdateTransactionDialog.value) {
                        UpdateTransaction(
                            openUpdateTransactionDialog = openUpdateTransactionDialog,
                            keyboardController = keyboardController,
                            viewModel = viewModel,
                            transaction = updateTransaction.value,
                            syncData = syncData
                        )
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
                                var cash =
                                    viewModel.accountCashBalance.collectAsState().value?.balance
                                if (cash.toString().isNullOrEmpty()) {
                                    cash = 0.0
                                }
                                Text(
                                    text = "$${"%.2f".format(cash)}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
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
                                TransactionItem(transaction, onDeleteClicked = {
                                    deleteTransaction.value = transaction
                                    openDeleteTransactionDialog.value = true
                                }, onCardClicked = {
                                    openUpdateTransactionDialog.value = true
                                    updateTransaction.value = transaction
                                })
                            }
                        } else {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "No transactions found",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(64.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Your transactions list is empty",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Add transactions to start tracking them.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Button(onClick = { openAddTransactionDialog.value = true }) {
                                        Text(text = "Add transaction")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            SyncProcess(viewModel, syncData)
        }
        LogoutWarning(navController, logout, viewModel)
    }
}

@Composable
fun UpdateTransaction(
    openUpdateTransactionDialog: MutableState<Boolean>,
    keyboardController: SoftwareKeyboardController?,
    viewModel: HomeScreenViewModel,
    transaction: Transaction,
    syncData: MutableState<Boolean>
) {
    var transactionAmount = remember {
        mutableStateOf(transaction.amount)
    }

    var transactionAmountText = remember {
        mutableStateOf(transactionAmount.value.toString())
    }

    var transactionName = remember {
        mutableStateOf(transaction.title)
    }

    var transactionIsExpense = remember {
        mutableStateOf(transaction.isExpense)
    }

    var context = LocalContext.current

    ShowDialogHomeScreen(
        title = "Update transaction",
        openDialog = openUpdateTransactionDialog,
        content = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Update Transaction Name",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

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
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFECECEC))
                        .padding(4.dp)
                ) {
                    Text(
                        "Income",
                        modifier = Modifier
                            .weight(1f)
                            .clickable { transactionIsExpense.value = false }
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (!transactionIsExpense.value) MaterialTheme.colorScheme.primary else Color.Transparent
                            )
                            .padding(8.dp),
                        color = if (!transactionIsExpense.value) Color.White else Color.Black,
                        textAlign = TextAlign.Center)

                    Text(
                        "Expense",
                        modifier = Modifier
                            .weight(1f)
                            .clickable { transactionIsExpense.value = true }
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (transactionIsExpense.value) WarningRed else Color.Transparent
                            )
                            .padding(8.dp),
                        color = if (transactionIsExpense.value) Color.White else Color.Black,
                        textAlign = TextAlign.Center)
                }

                Text(text = "Update transaction amount")
                OutlinedTextField(
                    value = transactionAmountText.value.toString(),
                    onValueChange = { input ->
                        if (input.isEmpty() || input.matches(Regex("^\\d+(\\.\\d{0,2})?$"))) {
                            transactionAmountText.value = input
                            transactionAmount.value = input.toDoubleOrNull() ?: 0.0
                        }
                    },
                    label = { Text("Enter new amount") },
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
        },
        onYesPressed = {
            if (transactionName.value.isEmpty() || transactionAmount.value.toString().isEmpty()) {
                Toast.makeText(
                    context, "Please enter the transaction's name and amount.", Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.removeTransaction(transaction)
                viewModel.addTransaction(
                    Transaction(
                        title = transactionName.value,
                        amount = transactionAmount.value,
                        isExpense = transactionIsExpense.value
                    )
                )
                openUpdateTransactionDialog.value = false
                syncData.value = true
            }
        })
}

@Composable
fun DeleteTransaction(
    deleteTranscation: MutableState<Boolean>,
    viewModel: HomeScreenViewModel,
    transaction: Transaction,
    syncData: MutableState<Boolean>
) {
    if (deleteTranscation.value) {
        ShowAlertDialog(
            title = "Delete transaction?",
            text = "Any deleted transactions cannot be recovered.",
            deleteTranscation
        ) {
            viewModel.removeTransaction(transaction)
            deleteTranscation.value = false
            syncData.value = true
        }
    }
}


@Composable
fun LogoutWarning(
    navController: NavController, logout: MutableState<Boolean>, viewModel: HomeScreenViewModel
) {
    val openDialog = remember { mutableStateOf(false) }
    if (logout.value) {
        logout.value = false
        openDialog.value = true
    }
    if (openDialog.value) {
        ShowAlertDialog(
            title = "Warning! Any unsaved data will be lost!",
            text = "Data that has been saved without a wifi connection might get lost.",
            openDialog = openDialog // now this is managed state
        ) {
            viewModel.clearData()
            FirebaseAuth.getInstance().signOut()
            navController.navigate(AppScreens.LoginScreen.name)
        }
    }
}

@Composable
private fun UpdatingBalanceProcess(
    openDialog: MutableState<Boolean>,
    keyboardController: SoftwareKeyboardController?,
    viewModel: HomeScreenViewModel,
    syncData: MutableState<Boolean>
) {
    var newBalance = remember {
        mutableStateOf(0.0)
    }
    val inputText = remember {
        mutableStateOf(newBalance.value.toString())
    }
    ShowDialogHomeScreen(title = "Update Balance", openDialog = openDialog, content = {
        Text(text = "Enter new balance:")
        OutlinedTextField(
            value = inputText.value.toString(),
            onValueChange = { input ->
                if (input.isEmpty() || input.matches(Regex("^\\d+(\\.\\d{0,2})?$"))) {
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
        syncData.value = true
    })
}

@Composable
private fun AddingTransactionProcess(
    openAddDialog: MutableState<Boolean>,
    keyboardController: SoftwareKeyboardController?,
    viewModel: HomeScreenViewModel,
    syncData: MutableState<Boolean>
) {
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

    ShowDialogHomeScreen(title = "Add transaction", openDialog = openAddDialog, content = {
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
                val unselectedColor = Color.Gray

                Text(
                    "Income",
                    modifier = Modifier
                        .weight(1f)
                        .clickable { transactionIsExpense.value = false }
                        .background(if (!transactionIsExpense.value) Color.Green else unselectedColor)
                        .padding(8.dp),
                    color = Color.White,
                    textAlign = TextAlign.Center)
                Text(
                    "Expense",
                    modifier = Modifier
                        .weight(1f)
                        .clickable { transactionIsExpense.value = true }
                        .background(if (transactionIsExpense.value) Color.Red else unselectedColor)
                        .padding(8.dp),
                    color = Color.White,
                    textAlign = TextAlign.Center)
            }

            Text(text = "Add amount")
            OutlinedTextField(
                value = transactionAmountText.value.toString(),
                onValueChange = { input ->
                    if (input.isEmpty() || input.matches(Regex("^\\d+(\\.\\d{0,2})?$"))) {
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
        if (transactionName.value.isEmpty() || transactionAmount.value.toString().isEmpty()) {
            Toast.makeText(
                context, "Please enter the transaction's name and amount.", Toast.LENGTH_SHORT
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
            syncData.value = true
        }
    })
}

@Composable
private fun SyncProcess(
    viewModel: HomeScreenViewModel,
    syncData: MutableState<Boolean>,
) {
    val transactions = viewModel.transaction_list.collectAsState()
    val acc = viewModel.accountCashBalance.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(syncData.value, transactions.value, acc.value) {
        if (syncData.value && acc.value != null) {
            syncDataToFireBase(
                context = context,
                transactions = transactions.value,
                account = acc.value!!,
            )
            syncData.value = false
        }
    }
}

@Composable
fun DrawerItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    iconTint: Color = MaterialTheme.colorScheme.primary
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}