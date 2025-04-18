package com.example.financeapptestversion.screens.details

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.financeapptestversion.components.DetailsButton
import com.example.financeapptestversion.components.FinanceAppBar
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
            FinanceAppBar(
                title = "Stock Details",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController
            ) {
                navController.popBackStack()
            }
        }
    ) {

        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (stockInfo.value != null) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val imgUrl =
                            "https://images.financialmodelingprep.com/symbol/${stockSymbol}.png"
                        Image(
                            painter = rememberAsyncImagePainter(model = imgUrl),
                            contentDescription = "Stock Image",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Text(
                            text = stockInfo?.value?.symbol.orEmpty(),
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "$${stockInfo?.value?.price}",
                            style = TextStyle(
                                fontSize = 18.sp,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            text = "Volume: ${stockInfo?.value?.volume}",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        )

                        Spacer(modifier = Modifier.height(40.dp))
                        if (buttons) {
                            var numberText by remember { mutableStateOf("") }
                            //var price by remember { mutableStateOf<Double?>(null) }

                            var quantityText by remember { mutableStateOf("") }
                            //var quantity by remember { mutableStateOf<Int?>(null) }

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "Enter Buy Price",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        TextField(
                                            value = numberText,
                                            onValueChange = { input ->
                                                if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                                    numberText = input
                                                }
                                            },
                                            label = { Text("Price") },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "Enter Quantity",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        TextField(
                                            value = quantityText,
                                            onValueChange = { input ->
                                                if (input.all { it.isDigit() }) {
                                                    quantityText = input
                                                }
                                            },
                                            label = { Text("Quantity") },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    DetailsButton(label = "Save") {

                                        if (numberText.isEmpty() || quantityText.isEmpty()) {

                                        } else {
                                            var savedStock = MStockItem(
                                                symbol = stockInfo.value!!.symbol,
                                                price = stockInfo.value!!.price,
                                                date = stockInfo.value!!.date,
                                                volume = stockInfo.value!!.volume,
                                                priceBought = numberText.toDouble(),
                                                quantityBought = quantityText.toInt(),
                                                userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                                            )
                                            saveToFirebase(savedStock, navController)
                                        }

                                    }
                                    DetailsButton(label = "Cancel") {
                                        navController.popBackStack()
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

//                                price?.let {
//                                    Text(
//                                        text = "Saved price: $it",
//                                        style = MaterialTheme.typography.bodyMedium
//                                    )
//                                }
//
//                                quantity?.let {
//                                    Text(
//                                        text = "Saved quantity: $it",
//                                        style = MaterialTheme.typography.bodyMedium
//                                    )
//                                }


                            }
                        }

                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Loading stock data...")
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

    }


}
