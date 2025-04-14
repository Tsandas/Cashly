package com.example.financeapptestversion.screens.search

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.financeapptestversion.components.FinanceAppBar
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.navigation.AppScreens


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

                    viewModel.searchStockII(symbol)

                }

                Spacer(modifier = Modifier.height(13.dp))

                StockList(navController, viewModel)


            }

        }

    }


}

@Composable
fun StockList(navController: NavController, viewModel: StockSearchViewModel) {

    for (i in viewModel.stocksInitList){
        if (i.value.loading == true){
            CircularProgressIndicator()
        }
    }

    val listOfStocks = viewModel.stocksInitList

    Log.d("BOO", "$listOfStocks")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {

        items(items = listOfStocks) { stock ->
           StockRow(stock = stock.value.data, navController)
        }

    }

}

@Composable
fun StockRow(stock: MStockItem?, x1: NavController) {
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
            val imgUrl = "https://images.financialmodelingprep.com/symbol/${stock?.symbol}.png"
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
                    text = stock?.symbol.toString(),
                    style = TextStyle(fontSize = 13.sp),
                    overflow = TextOverflow.Clip
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stock?.price.toString(),
                    style = TextStyle(fontSize = 13.sp),
                    overflow = TextOverflow.Clip
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Volume: ${stock?.volume.toString()}",
                    style = TextStyle(fontSize = 13.sp),
                    overflow = TextOverflow.Clip
                )
            }

        }
        Spacer(modifier = Modifier.height(40.dp))

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