package com.example.financeapptestversion.screens.stats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.financeapptestversion.components.FinanceAppBar
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.navigation.AppScreens
import com.example.financeapptestversion.screens.stocks.StockScreenViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun FinanceStatsScreen(
    navController: NavController, viewModel: StockScreenViewModel = hiltViewModel()
) {

    var stocks: List<MStockItem>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(
        topBar = {
            FinanceAppBar(
                title = "My Stats",
                showProfile = false,
                navController = navController,
                icon = Icons.Default.ArrowBack
            ){
                //navController.navigate(AppScreens.StocksScreen.name)
                navController.popBackStack()
            }
        }) {
        Surface(
            modifier = Modifier.padding(it)
        ) {

            stocks = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.toList().filter { mStock ->
                    mStock.userId == currentUser?.uid.toString()
                }
            } else {
                emptyList()
            }

            Column {
                Row(

                ) {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .padding(4.dp)
                    ) {
                        Icon(imageVector = Icons.Sharp.Person, contentDescription = "User Icon")
                    }
                    Text(text = "Hi ${currentUser?.email.toString().split("@")[0].uppercase()}")
                }
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                ) {

                    val ownedStocks: List<MStockItem> =
                        if (!viewModel.data.value.data.isNullOrEmpty()) {
                            stocks.filter { mStock ->
                                mStock.userId == currentUser?.uid.toString()
                            }
                        } else {
                            emptyList()
                        }


                    if(viewModel.data.value.loading==true) {
                        CircularProgressIndicator()
                    }else{
                        for (stock in ownedStocks) {
                            Text(
                                text = "${stock.symbol}, total shares: ${stock.quantityBought}",
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                }
            }


        }

    }

}