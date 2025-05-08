package com.example.financeapptestversion.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.financeapptestversion.components.BottomBar
import com.example.financeapptestversion.components.FinanceAppBar
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.navigation.AppScreens
import com.example.financeapptestversion.utils.Constants.AVAILABLE_SYMBOLS


@Composable
fun FinanceSearchScreen(
    navController: NavController, viewModel: StockSearchViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        FinanceAppBar(
            title = "Search for stocks",
            icon = Icons.Default.ArrowBack,
            onIconClicked = {
                navController.navigate(AppScreens.StocksScreen.name)
            },
            navController = navController,
            infoIconAction = { navController.navigate(AppScreens.AboutScreen.name) })
    }, bottomBar = {
        BottomBar(navController)
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
                ) { symbol ->
                    if (AVAILABLE_SYMBOLS.contains(symbol.uppercase())) {
                        viewModel.searchStockII(symbol)
                        true
                    } else {
                        false
                    }
                }
                Spacer(modifier = Modifier.height(13.dp))
                StockList(navController, viewModel)
            }
        }
    }
}

@Composable
fun StockList(navController: NavController, viewModel: StockSearchViewModel) {
    val listOfStocks = viewModel.stocksInitList
    Column(modifier = Modifier.fillMaxSize()) {
        if (viewModel.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = listOfStocks) { stock ->
                StockRow(stock = stock.value.data, navController)
            }
        }
    }
}

@Composable
fun StockRow(stock: MStockItem?, navController: NavController) {
    val cardColor = if (isSystemInDarkTheme()) Color(0xFF2E7D32) else Color(0xFFC8E6C9)
    val imageBackground = Color(0xFFE0E0E0)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(AppScreens.DetailScreen.name + "/${stock?.symbol}")
            }
            .padding(vertical = 6.dp),

        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imgUrl = "https://images.financialmodelingprep.com/symbol/${stock?.symbol}.png"
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = imgUrl),
                    contentDescription = "${stock?.symbol} logo",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (stock == null) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = stock.symbol,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "$${stock.price}", style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = "Volume: ${stock.volume}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    viewModel: StockSearchViewModel,
    onSearch: (String) -> Boolean // Return true if valid, false otherwise
) {
    val searchQueryState = rememberSaveable { mutableStateOf("") }
    val showError = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchQueryState.value,
                onValueChange = {
                    searchQueryState.value = it
                    showError.value = false
                },
                placeholder = { Text(hint) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                singleLine = true,
                isError = showError.value,
                shape = RoundedCornerShape(12.dp),
                keyboardActions = KeyboardActions(
                    onDone = {
                        val symbol = searchQueryState.value.trim().uppercase()
                        if (symbol.isNotEmpty()) {
                            val valid = onSearch(symbol)
                            showError.value = !valid
                            if (valid) {
                                searchQueryState.value = ""
                                keyboardController?.hide()
                            }
                        }
                    })
            )

            Button(
                onClick = {
                    val symbol = searchQueryState.value.trim().uppercase()
                    if (symbol.isNotEmpty()) {
                        val valid = onSearch(symbol)
                        showError.value = !valid
                        if (valid) {
                            searchQueryState.value = ""
                            keyboardController?.hide()
                        }
                    }
                }, enabled = !loading, shape = RoundedCornerShape(12.dp)
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        }

        if (showError.value) {
            Text(
                text = "Symbol not available.",
                color = Color.Red,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}