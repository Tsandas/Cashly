package com.example.financeapptestversion.screens.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.financeapptestversion.components.BottomBar
import com.example.financeapptestversion.components.FinanceAppBar
import com.example.financeapptestversion.ui.theme.CardBackground
import com.example.financeapptestversion.utils.Constants.AVAILABLE_SYMBOLS

@Composable
fun FinanceAboutScreen(navController: NavController) {

    Scaffold(topBar = {
        FinanceAppBar(
            title = "About",
            navController = navController,
            icon = Icons.Default.ArrowBack,
            onIconClicked = {
                navController.popBackStack()
            })
    }, bottomBar = { BottomBar(navController) }) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {


            Text(
                text = "Welcome to Cashly!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Cashly is your smart finance companion. Track expenses and manage your stock portfolio effortlessly.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            InfoCard(
                title = "💸 Expense Tracking",
                content = "Sync expenses across devices in real-time. Add, edit, and categorize with ease."
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your expenses are synced securely across all your devices in real-time. Add, edit, and categorize your spending with ease."
            )

            Spacer(modifier = Modifier.height(20.dp))

            InfoCard(
                title = "📈 Stock Portfolio",
                content = "Monitor your favorite stocks in-app. Currently supported stocks:"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp)
            ) {
                LazyRow(
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(AVAILABLE_SYMBOLS) { symbol ->
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(50)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = symbol,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }


//            Card(
//                shape = RoundedCornerShape(16.dp),
//                colors = CardDefaults.cardColors(containerColor = CardBackground),
//                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .heightIn(min = 120.dp)
//            ) {
//                LazyColumn(modifier = Modifier.padding(16.dp)) {
//                    items(AVAILABLE_SYMBOLS) { symbol ->
//                        Text(
//                            text = "• $symbol",
//                            style = MaterialTheme.typography.bodyLarge,
//                            modifier = Modifier.padding(vertical = 4.dp),
//                            color = Color.Black
//                        )
//                    }
//                }
//            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Thanks for choosing Cashly!",
                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
            )

        }

    }

}

@Composable
fun InfoCard(title: String, content: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = content, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
        }
    }
}