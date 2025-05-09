package com.example.financeapptestversion.screens.stocks

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.financeapptestversion.components.BottomBar
import com.example.financeapptestversion.components.FinanceAppBar
import com.example.financeapptestversion.components.TitleSection
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.navigation.AppScreens
import com.example.financeapptestversion.ui.theme.LossRedLightBackground
import com.example.financeapptestversion.ui.theme.ProfitGreenLightBackground
import com.example.financeapptestversion.utils.toAbbreviated
import com.google.firebase.auth.FirebaseAuth

//https://site.financialmodelingprep.com/developer/docs/stable

@Composable
fun FinanceStocksScreen(
    navController: NavController, viewModel: StockScreenViewModel = hiltViewModel()
) {

    Scaffold(topBar = {
        FinanceAppBar(
            title = "Cashly",
            navController = navController,
            icon = Icons.Default.ArrowBack,
            onIconClicked = {
                navController.navigate(AppScreens.HomeScreen.name)
            },
            infoIconAction = {
                navController.navigate(AppScreens.AboutScreen.name)
            })
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                navController.navigate(AppScreens.SearchScreen.name)
            }, containerColor = Color(0xFF4CAF50), contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Transaction")
        }
    }, bottomBar = {
        BottomBar(navController, stocksScreen = true)
    }) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StockScreenMainContent(navController, viewModel)
        }
    }
}

@Composable
fun StockScreenMainContent(navController: NavController, viewModel: StockScreenViewModel) {

    var listOfStocks = emptyList<MStockItem>()
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (!viewModel.data.value.data.isNullOrEmpty()) {
        listOfStocks = viewModel.data.value.data!!.toList().filter { mStock ->
            mStock.userId == currentUser?.uid.toString()
        }
    }

    val totalInvested =
        listOfStocks.sumOf { it.priceBought?.times((it.quantityBought ?: 0)) ?: 0.0 }
    val totalProfit =
        listOfStocks.sumOf { (it.price - (it.priceBought ?: 0.0)).times((it.quantityBought ?: 0)) }
    val totalValue = totalInvested + totalProfit

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        TitleSection(label = "Portfolio Stats")
        PortfolioSummaryCard(totalInvested, totalValue, totalProfit)

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
        ) {
            TitleSection(label = "My Stocks")
            Spacer(modifier = Modifier.fillMaxWidth(1f))
        }
        Spacer(Modifier.height(10.dp))

        if (viewModel.stocksState.value.loading == true) {
            CircularProgressIndicator()
        } else {
            if (listOfStocks.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Empty Portfolio",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Your portfolio is empty",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add stocks to start tracking your investments.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = { navController.navigate(AppScreens.SearchScreen.name) }) {
                        Text(text = "Add Stocks")
                    }
                }
            } else {
                StockListArea(listOfStocks, navController)
            }
            Spacer(Modifier.height(40.dp))
        }

    }

}

@Composable
fun StockListArea(
    listOfStocks: List<MStockItem>, navController: NavController
) {
    HorizontalScrollableComponent(listOfStocks) {
        navController.navigate(AppScreens.UpdateScreen.name + "/$it")
    }
}

@Composable
fun HorizontalScrollableComponent(
    listOfStocks: List<MStockItem>, onCardPressed: (String) -> Unit
) {
    val scrollState = rememberScrollState()
//    LazyRow(
//        Modifier.fillMaxWidth()
//    ) {
//        items(
//            count = listOfStocks.count(), itemContent = { index ->
//                val stock = listOfStocks[index]
//
//                ListCard(stock = stock) {
//                    onCardPressed(stock.id.toString())
//                }
//
//            })
//    }


    LazyHorizontalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .height(440.dp),
        rows = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            count = listOfStocks.count(), itemContent = { index ->
                val stock = listOfStocks[index]
                ListCard(stock = stock) {
                    onCardPressed(stock.id.toString())
                }
            })
    }


}

@Composable
fun ListCard(
    stock: MStockItem, onPressDetails: (String) -> Unit = {}
) {

    val profitPercent = stock.priceBought?.let {
        ((stock.price - it) / it) * 100
    }
    val backgroundColor = when {
        profitPercent == null -> MaterialTheme.colorScheme.surface
        profitPercent >= 0 -> ProfitGreenLightBackground
        else -> LossRedLightBackground
    }
    val profitPerShare = stock.price - stock.priceBought!!  // This always results in 0
    val totalProfit = profitPerShare * (stock.quantityBought ?: 0)

    val titleColor = Color.Black
//        when {
//        profitPercent == null -> MaterialTheme.colorScheme.surface
//        profitPercent >= 0 -> Color.Green
//        else -> Color.Red
//    }

    //Symbol Limited to AAPL, TSLA, AMZN, MSFT, NVDA, GOOGL, META, NFLX, JPM, V, BAC, AMD,
    // PYPL, DIS, T, PFE, COST, INTC, KO, TGT, NKE, SPY, BA, BABA, XOM, WMT, GE, CSCO, VZ,
    // JNJ, CVX, PLTR, SQ, SHOP, SBUX, SOFI, HOOD, RBLX, SNAP, AMD, UBER, FDX, ABBV, ETSY,
    // MRNA, LMT, GM, F, RIVN, LCID, CCL, DAL, UAL, AAL, TSM, SONY, ET, NOK, MRO, COIN, RIVN,
    // SIRI, SOFI, RIOT, CPRX, PYPL, TGT, VWO, SPYG, NOK, ROKU, HOOD, VIAC, ATVI, BIDU, DOCU,
    // ZM, PINS, TLRY, WBA, VIAC, MGM, NFLX, NIO, C, GS, WFC, ADBE, PEP, UNH, CARR, FUBO, HCA,
    // TWTR, BILI, SIRI, VIAC, FUBO, RKT

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .width(200.dp)
            .clickable {
                onPressDetails.invoke(stock.id.toString())
            }) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    painter = rememberAsyncImagePainter("https://images.financialmodelingprep.com/symbol/${stock.symbol}.png"),
                    contentDescription = "${stock.symbol} logo",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 12.dp)
                )
            }
            Text(
                text = stock.symbol ?: "N/A",
                style = MaterialTheme.typography.titleLarge,
                color = titleColor
            )
            Text(
                text = "Current price: $${"%.2f".format(stock.price)}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
            stock.priceBought?.let { buyPrice ->
                val profit = ((stock.price - buyPrice) / buyPrice * 100)
                Text(
                    text = if (profit >= 0) "Profit: ↑ +%.2f%%, $${"%.2f".format(totalProfit)}".format(
                        profit
                    ) else "Loss: ↓ %.2f%%, -$${
                        "%.2f".format(
                            totalProfit*(-1)
                        )
                    }".format(profit),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (profit >= 0) Color(0xFF00C853) else Color.Red
                )
            }

        }

    }

}


@Composable
fun PortfolioSummaryCard(
    totalInvested: Double, totalValue: Double, totalProfit: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Text(
                text = "My Portfolio",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
            ) {
                StatColumn("Current", totalValue)
                StatColumn("Invested", totalInvested)
                StatColumn("Profit", totalProfit)
            }
        }
    }
}

@Composable
fun StatColumn(label: String, value: Double) {
    val isProfit = value >= 0
    val color = if (label == "Profit") {
        if (isProfit) Color(0xFF00C853) else Color(0xFFD50000)
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value.toAbbreviated(),
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 26.sp),
            color = color,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}
