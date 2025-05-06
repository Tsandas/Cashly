package com.example.financeapptestversion.screens.details

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.financeapptestversion.components.BottomBar
import com.example.financeapptestversion.components.FinanceAppBarII
import com.example.financeapptestversion.components.RoundedButtonI
import com.example.financeapptestversion.model.MStockItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FinanceStockDetailsScreen(
    navController: NavController,
    stockSymbol: String,
    viewModel: DetailsViewModel = hiltViewModel(),
    buttons: Boolean = true
) {

    val stockInfo = remember {
        mutableStateOf<MStockItem?>(null)
    }
    LaunchedEffect(stockSymbol) {
        stockInfo.value = viewModel.getStockInfo(stockSymbol).data
    }

    Scaffold(
        topBar = {
            FinanceAppBarII(
                title = "Stock Details",
                icon = Icons.Default.ArrowBack,
                navController = navController,
                onBackArrowClicked = {
                    navController.popBackStack()
                }
            )
        },
        bottomBar = {
            BottomBar(navController)
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (stockInfo.value == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Loading stock data...")
                        }
                    }
                } else {
                    val stock = stockInfo.value!!
                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .background(Color(0xFFE0E0E0), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = "https://images.financialmodelingprep.com/symbol/${stock.symbol}.png"
                                ),
                                contentDescription = "Stock Image",
                                modifier = Modifier.size(100.dp)
                            )
                        }

                        Text(
                            text = stock.symbol,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$${stock.price}",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Volume: ${stock.volume}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (buttons) {
                            var numberText by remember { mutableStateOf("") }
                            var quantityText by remember { mutableStateOf("") }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "Enter Buy Price",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    TextField(
                                        value = numberText,
                                        onValueChange = {
                                            if (it.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                                numberText = it
                                            }
                                        },
                                        label = { Text("Price") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "Enter Quantity",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    TextField(
                                        value = quantityText,
                                        onValueChange = {
                                            if (it.all { ch -> ch.isDigit() }) quantityText = it
                                        },
                                        label = { Text("Quantity") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                RoundedButtonI(label = "Save") {
                                    if (numberText.isNotEmpty() && quantityText.isNotEmpty()) {
                                        val savedStock = MStockItem(
                                            symbol = stock.symbol,
                                            price = stock.price,
                                            date = stock.date,
                                            volume = stock.volume,
                                            priceBought = numberText.toDouble(),
                                            quantityBought = quantityText.toInt(),
                                            userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
                                        )
                                        saveToFirebase(savedStock, navController)
                                    }
                                }
                                RoundedButtonI(label = "Cancel") {
                                    navController.popBackStack()
                                }
                            }
                        }
                    }
                }
            }

        }

    }

}


private fun saveToFirebase(savedStock: MStockItem, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("stocks")

    if (savedStock.toString().isNotEmpty()) {
        dbCollection.add(savedStock)
            .addOnSuccessListener { documentReference ->
                val docId = documentReference.id
                dbCollection.document(docId)
                    .update("id", docId)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.popBackStack()
                        }
                    }
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error writing document", e)
            }

    } else {
        //todo
    }

}
