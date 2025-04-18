package com.example.financeapptestversion.screens.update

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.financeapptestversion.components.FinanceAppBar
import com.example.financeapptestversion.data.DataOrException
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.screens.stocks.StockScreenViewModel

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
            ){
                navController.popBackStack()
            }
        }) {
        val stockInfo =
            produceState<DataOrException<List<MStockItem>, Boolean, Exception>>(
                initialValue = DataOrException(
                    data = emptyList(),
                    true,
                    Exception("")
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

                Log.d("info", "updatescreen ${viewModel.data.value.data.toString()}")
                if(stockInfo.loading == true){
                    CircularProgressIndicator()
                    stockInfo.loading = false
                }else{
                    Text(text = viewModel.data.value.data.toString())
                }

            }
        }

    }

}