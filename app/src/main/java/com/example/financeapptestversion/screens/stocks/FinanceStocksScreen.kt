package com.example.financeapptestversion.screens.stocks

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.financeapptestversion.components.FABContent
import com.example.financeapptestversion.components.FinanceAppBar
import com.example.financeapptestversion.components.TitleSection
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth

//https://site.financialmodelingprep.com/developer/docs/stable

@Composable
fun FinanceStocksScreen(
    navController: NavController, viewModel: StockScreenViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            FinanceAppBar(
                title = "Cashly",
                showProfile = true,
                navController = navController,
                icon = Icons.Default.ArrowBack
            ) {
                navController.navigate(AppScreens.HomeScreen.name)
            }
        },
        floatingActionButton = {
            FABContent {
                navController.navigate(AppScreens.SearchScreen.name)
            }
        }
    ) {
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

    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName = if (!email.isNullOrEmpty()) {
        email?.split("@")[0]
    } else {
        "N/A"
    }

    Column(
        modifier = Modifier.padding(2.dp, top = 40.dp), verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.align(alignment = Alignment.Start)
        ) {
            TitleSection(label = "My Stock Portfolio")

            Spacer(modifier = Modifier.fillMaxWidth(0.7f))

            Column(

            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(AppScreens.StatsScreen.name)
                        }
                        .size(25.dp),
                    tint = MaterialTheme.colorScheme.secondary)

                Text(
                    text = currentUserName!!,
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Divider()
            }
        }
        Spacer(Modifier.padding(top = 20.dp))

        //MyStocksArea(stocks = listOfStocks, navController )

        if (viewModel.stocksState.value.loading == true) {
            CircularProgressIndicator()
        } else {
            StockListArea(listOfStocks, navController)
            Spacer(Modifier.height(40.dp))
        }

        TitleSection(label = "Hot Stocks")
        Spacer(Modifier.height(65.dp))

        if (viewModel.stocksState.value.loading == true) {
            CircularProgressIndicator()
        } else {
            val tempHotStocks = listOf<MStockItem>(
                MStockItem(
                    symbol = "AAPL",
                    price = 124.1,
                    volume = 34,
                    date = "124"
                ),
                MStockItem(
                    symbol = "TSLA",
                    price = 124.1,
                    volume = 34,
                    date = "124"
                )
            )
            StockListArea(tempHotStocks, navController = navController)
        }

    }

}

@Composable
fun StockListArea(
    listOfStocks: List<MStockItem> = emptyList(), navController: NavController
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
    LazyRow(
        Modifier.fillMaxWidth()
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
fun MyStocksArea(stocks: List<MStockItem>, navController: NavController) {
    //ListCard()
}

@Composable
fun ListCard(
    stock: MStockItem, onPressDetails: (String) -> Unit = {}
) {

    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp

    //Symbol Limited to AAPL, TSLA, AMZN, MSFT, NVDA, GOOGL, META, NFLX, JPM, V, BAC, AMD, PYPL, DIS, T, PFE, COST, INTC, KO, TGT, NKE, SPY, BA, BABA, XOM, WMT, GE, CSCO, VZ, JNJ, CVX, PLTR, SQ, SHOP, SBUX, SOFI, HOOD, RBLX, SNAP, AMD, UBER, FDX, ABBV, ETSY, MRNA, LMT, GM, F, RIVN, LCID, CCL, DAL, UAL, AAL, TSM, SONY, ET, NOK, MRO, COIN, RIVN, SIRI, SOFI, RIOT, CPRX, PYPL, TGT, VWO, SPYG, NOK, ROKU, HOOD, VIAC, ATVI, BIDU, DOCU, ZM, PINS, TLRY, WBA, VIAC, MGM, NFLX, NIO, C, GS, WFC, ADBE, PEP, UNH, CARR, FUBO, HCA, TWTR, BILI, SIRI, VIAC, FUBO, RKT

    Card(
        shape = RoundedCornerShape(29.dp), colors = CardDefaults.cardColors(
            containerColor = Color.LightGray,
        ), elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ), modifier = Modifier
            .padding(16.dp)
            .height(260.dp)
            .width(220.dp)
            .clickable {
                onPressDetails.invoke(stock.id.toString())
            }) {
        Column(
            modifier = Modifier.width(screenWidth.dp - (spacing * 2))
        ) {
            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = "https://images.financialmodelingprep.com/symbol/${stock.symbol}.png"),
                    contentDescription = "Stock Image",
                    Modifier
                        .height(150.dp)
                        .width(110.dp)
                        .padding(4.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stock.symbol.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(4.dp),
                    maxLines = 1
                )

                Text(
                    text = "$${stock.price}",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(4.dp),
                    maxLines = 1
                )
            }

            stock.priceBought?.let { buyPrice ->
                val profit = (buyPrice - stock.price) / buyPrice * 100
                Text(
                    text = "Profit/Loss %.3f%%".format(profit),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(4.dp)
                )
            }

        }

    }

}
