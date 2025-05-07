package com.example.financeapptestversion.screens.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.financeapptestversion.components.BottomBar
import com.example.financeapptestversion.components.FinanceAppBarII
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.navigation.AppScreens
import com.example.financeapptestversion.ui.theme.LossRedLightBackground
import com.example.financeapptestversion.ui.theme.ProfitGreenLightBackground
import com.google.firebase.auth.FirebaseAuth


@Composable
fun FinanceStatsScreen(
    navController: NavController, viewModel: FinanceStatsScreenViewModel = hiltViewModel()
) {

    var stocks: List<MStockItem>
    val currentUser = FirebaseAuth.getInstance().currentUser
    val isLoading = viewModel.data.value.loading == true
    Scaffold(
        topBar = {
            FinanceAppBarII(
                title = "My Stats",
                navController = navController,
                icon = Icons.Default.ArrowBack,
                onIconClicked = {
                    navController.popBackStack()
                }
            )
        },
        bottomBar = { BottomBar(navController) }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            stocks = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.toList().filter { mStock ->
                    mStock.userId == currentUser?.uid.toString()
                }
            } else {
                emptyList()
            }

            Column(
                Modifier.padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Sharp.Person,
                        contentDescription = "User Icon",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = currentUser?.email?.split("@")?.get(0)?.uppercase() ?: "GUEST",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    elevation = CardDefaults.elevatedCardElevation(4.dp)
                ) {
                    Row {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Available Cash", style = MaterialTheme.typography.labelLarge)
                            Text(
                                text = "$${viewModel.localAccount.value.balance}",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (isLoading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                Text("Invested Amount", style = MaterialTheme.typography.labelLarge)
                                var totalInvested = 0.0
                                stocks.forEach { stock ->
                                    totalInvested += stock.priceBought!! * stock.quantityBought!!
                                }
                                Text(
                                    text = "$$totalInvested",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    elevation = CardDefaults.elevatedCardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Owned Stocks", style = MaterialTheme.typography.titleMedium)

                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            if (stocks.isEmpty()) {
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
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(vertical = 15.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    items(stocks) { stock ->
                                        val imgUrl =
                                            "https://images.financialmodelingprep.com/symbol/${stock?.symbol}.png"
                                        val profitPerShare =
                                            stock.price - stock.priceBought!!  // This always results in 0
                                        val totalProfit =
                                            profitPerShare * (stock.quantityBought ?: 0)
                                        val profitColor =
                                            if (totalProfit >= 0) Color(0xFF4CAF50) else Color(
                                                0xFFF44336
                                            )
                                        val backgroundColor =
                                            if (totalProfit >= 0) ProfitGreenLightBackground else LossRedLightBackground

                                        Card(
                                            shape = RoundedCornerShape(24.dp),
                                            colors = CardDefaults.cardColors(containerColor = backgroundColor),
                                            elevation = CardDefaults.cardElevation(8.dp),
                                            modifier = Modifier
                                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                                .clickable {
                                                    //onPressDetails.invoke(stock.id.toString())
                                                }) {

                                            Row(
                                                modifier = Modifier
                                                    .padding(horizontal = 8.dp, vertical = 10.dp)
                                                    .fillMaxWidth()
                                                    .background(
                                                        color = Color(0xFFF2F2F2),
                                                        shape = RoundedCornerShape(8.dp)
                                                    ),
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Image(
                                                    painter = rememberAsyncImagePainter(model = imgUrl),
                                                    contentDescription = "Stock Image",
                                                    modifier = Modifier
                                                        .size(60.dp)
                                                        .padding(10.dp)
                                                        .clip(RoundedCornerShape(8.dp))
                                                )

                                                Spacer(modifier = Modifier.width(8.dp))

                                                Column {
                                                    Text(
                                                        text = "${stock.symbol} – ${stock.quantityBought} shares",
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                    Text(
                                                        text = if (totalProfit >= 0) "Profit: $${
                                                            "%.2f".format(
                                                                totalProfit
                                                            )
                                                        }" else
                                                            "Loss: -$${"%.2f".format(totalProfit * -1)}",
                                                        color = profitColor,
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                }
                                                Spacer(modifier = Modifier.weight(1f))
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Edit stock",
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.clickable {
                                                        navController.navigate(AppScreens.UpdateScreen.name + "/${stock.id}")
                                                    }
                                                )
                                            }

                                        }

                                    }
                                }
                            }
                        }
                    }
                }


            }
        }

    }

}