package com.example.financeapptestversion.screens.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.financeapptestversion.components.FinanceAppBar
import com.example.financeapptestversion.components.InputField
import com.example.financeapptestversion.model.MStock
import com.example.financeapptestversion.navigation.AppScreens
import dagger.hilt.android.AndroidEntryPoint


@Composable
fun FinanceSearchScreen(navController: NavController, viewModel: StockSearchViewModel = hiltViewModel()) {

    Scaffold(
        topBar = {
            FinanceAppBar(
                title = "Search for stocks",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController
            ) {
                navController.navigate(AppScreens.StocksScreen.name)
            }
        }) {
        Surface(modifier = Modifier.padding(it)) {
            Column() {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    loading = false,
                    viewModel = viewModel,
                    hint = "Search"
                ) {symbol ->

                    viewModel.searchStocks(symbol)

                }

                Spacer(modifier = Modifier.height(13.dp))

                StockList(navController)


            }

        }

    }


}

@Composable
fun StockList(navController: NavController) {

    val listOfStocks = listOf<MStock>(
        MStock(
            id = "124",
            stockSymbol = "MSFT",
            stockName = "Apple Inc",
            stockPrice = 123.45
        ),
        MStock(
            id = "123",
            stockSymbol = "AAPL",
            stockName = "Apple Inc",
            stockPrice = 123.45
        ),
        MStock(
            id = "12112",
            stockSymbol = "BOSS",
            stockName = "Apple Inc",
            stockPrice = 1212.45
        ),
        MStock(
            id = "123",
            stockSymbol = "NFLX",
            stockName = "Apple Inc",
            stockPrice = 123.45
        )

    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {

        items(items = listOfStocks) { stock ->
            StockRow(stock = stock, navController)
        }

    }

}

@Composable
fun StockRow(stock: MStock, x1: NavController) {

    Card(
        modifier = Modifier
            .clickable {}
            .fillMaxWidth()
            .height(100.dp)
            .padding(3.dp),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(7.dp)
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top,
        ) {
            val imgUrl = "https://images.financialmodelingprep.com/symbol/AAPL.png"
            Image(
                painter = rememberAsyncImagePainter(model = imgUrl),
                contentDescription = "Stock Image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(4.dp)
            )
            Column() {
                Text(
                    text = stock.stockName.toString(),
                    style = TextStyle(fontSize = 13.sp),
                    overflow = TextOverflow.Clip
                )
                Text(
                    text = stock.stockSymbol.toString(),
                    style = TextStyle(fontSize = 13.sp),
                    overflow = TextOverflow.Clip
                )
                Text(
                    text = stock.stockPrice.toString(),
                    style = TextStyle(fontSize = 13.sp),
                    overflow = TextOverflow.Clip
                )
            }

        }

    }

}

@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    viewModel: StockSearchViewModel,
    onSearch: (String) -> Unit = {}
) {
    Column() {
        val searchQuerryState = rememberSaveable {
            mutableStateOf("")
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQuerryState.value) {
            searchQuerryState.value.trim().isNotEmpty()
        }

//        InputField(
//            valueState = searchQuerryState,
//            labelId = "Search",
//            enabled = true,
//            onAction = KeyboardActions {
//                if (!valid) return@KeyboardActions
//                onSearch(searchQuerryState.value.trim())
//                searchQuerryState.value = ""
//                keyboardController?.hide()
//            }
//        )
        OutlinedTextField(
            value = searchQuerryState.value,
            onValueChange = { searchQuerryState.value = it },
            label = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            enabled = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    if (!valid) return@KeyboardActions
                    onSearch(searchQuerryState.value.trim())
                    searchQuerryState.value = ""
                    keyboardController?.hide()
                }
            )
        )


    }

}