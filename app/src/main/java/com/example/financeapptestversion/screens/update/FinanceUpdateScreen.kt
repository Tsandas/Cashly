package com.example.financeapptestversion.screens.update

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.financeapptestversion.R
import com.example.financeapptestversion.components.FinanceAppBar
import com.example.financeapptestversion.components.RoundedButton
import com.example.financeapptestversion.components.showToast
import com.example.financeapptestversion.data.DataOrException
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.navigation.AppScreens
import com.example.financeapptestversion.screens.stocks.StockScreenViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FinanceUpdateScreen(
    navController: NavController,
    stockItemId: String,
    viewModel: StockScreenViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            FinanceAppBar(
                title = "Update Stock",
                showProfile = false,
                navController = navController,
                icon = Icons.Default.ArrowBack
            ) {
                navController.popBackStack()
            }
        }) {

        val stockInfo = produceState<DataOrException<List<MStockItem>, Boolean, Exception>>(
            initialValue = DataOrException(
                data = emptyList(), true, Exception("")
            )
        ) {
            value = viewModel.data.value
        }.value



        Surface(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                if (stockInfo.loading == true) {
                    CircularProgressIndicator()
                    stockInfo.loading = false
                } else {

                    Surface(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        // shape = CircleShape,
                        // shadowElevation = 4.dp
                    ) {
                        //Text("Stock Info ${stockInfo.data?.get(0)} and ${stockItemId}")

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ShowBookUpdate(stockInfo = viewModel.data.value, stockItemId)

                            ShowUpdateFields(stock = viewModel.data.value.data?.first { mStock ->
                                mStock.id == stockItemId
                            }, navController)


                        }

                    }

                }

            }
        }

    }

}

@Composable
fun ShowUpdateFields(stock: MStockItem?, navController: NavController) {

    val priceText = remember { mutableStateOf("") }
    val quantityText = remember { mutableStateOf("") }

    updateFields(
        defaultBuyPrice = if (stock?.priceBought.toString()
                .isNotEmpty()
        ) stock?.priceBought.toString() else "",
        defaultQuantity = if (stock?.quantityBought.toString()
                .isNotEmpty()
        ) stock?.quantityBought.toString() else "",
    ) { newBuyPrice, newQuantity ->
        priceText.value = newBuyPrice
        quantityText.value = newQuantity
    }

    Spacer(Modifier.height(20.dp))

    Row(
    ) {
        val changedPrice = stock?.priceBought.toString() != priceText.value
        val changedQuantity = stock?.quantityBought.toString() != quantityText.value

        val stockUpdate = changedQuantity || changedPrice

        val context = LocalContext.current


        val stockToUpdate = hashMapOf(
            "price_bought" to priceText.value.toDoubleOrNull(),
            "quantity_bought" to quantityText.value.toDoubleOrNull()
        ).toMap()
        RoundedButton(label = "Update") {
            if (stockUpdate) {
                Log.d(
                    "TAG",
                    "updates stuff price: ${priceText.value}, quantity: ${quantityText.value}"
                )
                FirebaseFirestore.getInstance().collection("stocks").document(stock?.id.toString())
                    .update(stockToUpdate).addOnCompleteListener { task ->
                        showToast(context, "Stock Updated Successfully!")
                        navController.navigate(AppScreens.StocksScreen.name)
                    }.addOnFailureListener {
                        showToast(context, "An error occurred, please try again later.")
                    }
            }
        }
        Spacer(Modifier.width(20.dp))
        val openDialog = remember {
            mutableStateOf(false)
        }
        if (openDialog.value) {
            ShowAlertDialog(
                text = stringResource(id = R.string.delete_message) + "\n" + stringResource(
                    id = R.string.action_delete
                ), openDialog
            ) {
                FirebaseFirestore.getInstance().collection("stocks").document(stock?.id.toString())
                    .delete().addOnCompleteListener {
                        if (it.isSuccessful) {
                            openDialog.value = false
                            navController.navigate(AppScreens.StocksScreen.name)
                        }
                    }.addOnFailureListener {
                        showToast(context, "An error occurred, please try again later.")
                    }
            }
        }
        RoundedButton(label = "Delete") {
            openDialog.value = true
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowAlertDialog(text: String, openDialog: MutableState<Boolean>, onYesPressed: () -> Unit) {

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false},
            title = { Text(text = "Remove stock") },
            text = { Text(text = text) },
            confirmButton = {
                TextButton(
                    onClick = { onYesPressed.invoke() }) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { openDialog.value = false }) {
                    Text(text = "No")
                }
            })
    }
}


@Composable
fun updateFields(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    defaultBuyPrice: String = "",
    defaultQuantity: String = "",
    onSearch: (String, String) -> Unit
) {

    val priceFieldValue = rememberSaveable {
        mutableStateOf(defaultBuyPrice)
    }

    val priceQuantityValue = rememberSaveable {
        mutableStateOf(defaultQuantity)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val validState = remember(
        priceFieldValue.value, priceQuantityValue.value
    ) {
        priceFieldValue.value.trim().isNotEmpty() && priceQuantityValue.value.trim().isNotEmpty()
    }

    Column(

    ) {

        fun tryUpdate() {
            if (validState) {
                onSearch(priceFieldValue.value.trim(), priceQuantityValue.value.trim())
                keyboardController?.hide()
            }
        }

        OutlinedTextField(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .background(Color.White),
            value = priceFieldValue.value,
            onValueChange = {
                priceFieldValue.value = it
                tryUpdate()
            },
            label = { Text("Enter new buy price") },
            singleLine = true,
            keyboardActions = KeyboardActions { tryUpdate() })

        OutlinedTextField(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .background(Color.White),
            value = priceQuantityValue.value,
            onValueChange = {
                priceQuantityValue.value = it
                tryUpdate()
            },
            label = { Text("Enter new quantity") },
            singleLine = true,
            keyboardActions = KeyboardActions { tryUpdate() })

//        InputField(
//            modifier = Modifier
//                .padding(4.dp)
//                .width(150.dp)
//                .height(25.dp)
//                .background(Color.White),
//            valueState = priceFieldValue,
//            labelId = "Enter new buy price",
//            enabled = true,
//            onAction = KeyboardActions {
//                tryUpdate()
//            })
//
//
//        InputField(
//            modifier = Modifier
//                .padding(4.dp)
//                .width(150.dp)
//                .height(25.dp)
//                .background(Color.White),
//            valueState = priceQuantityValue,
//            labelId = "Enter new quantity",
//            enabled = true,
//            onAction = KeyboardActions {
//                tryUpdate()
//            })

    }


}

@Composable
fun ShowBookUpdate(
    stockInfo: DataOrException<List<MStockItem>, Boolean, Exception>, stockItemId: String
) {

    Row(

    ) {
        Spacer(modifier = Modifier.width(43.dp))
        if (stockInfo.data != null) {
            Column(
                modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Center
            ) {

                CardListItem(stock = stockInfo.data!!.first { mStock ->
                    mStock.id == stockItemId
                }, onPressDetails = {})

            }
        }

    }


}

@Composable
fun CardListItem(stock: MStockItem, onPressDetails: () -> Unit) {

    Card(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp)
            .clip(
                RoundedCornerShape(20.dp)
            )
            .clickable {}, elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {

        Row(

        ) {
            Image(
                painter = rememberAsyncImagePainter(model = "https://images.financialmodelingprep.com/symbol/${stock?.symbol}.png"),
                contentDescription = "Stock Image",
                modifier = Modifier
                    .height(100.dp)
                    .width(120.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp))
            )

            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = stock.symbol)
                Text(text = "Price bought: ${stock.priceBought}")
                Text(text = "Quantity bought: ${stock.quantityBought}")
            }

        }


    }

}