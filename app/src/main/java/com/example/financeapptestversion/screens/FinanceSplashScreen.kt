package com.example.financeapptestversion.screens

import android.util.Log
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.financeapptestversion.model.AccountCashBalance
import com.example.financeapptestversion.model.Transaction
import com.example.financeapptestversion.navigation.AppScreens
import com.example.financeapptestversion.ui.theme.CardBackground
import com.example.financeapptestversion.ui.theme.SoftGray
import com.example.financeapptestversion.utils.isConnectedToWifi
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import com.example.financeapptestversion.R
import com.example.financeapptestversion.components.AppLogo

@Composable
fun FinanceSplashScreen(
    navController: NavController,
    viewModel: SplashScreenViewModel = hiltViewModel(),
) {
    var context = LocalContext.current
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(true) {
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(
                delayMillis = 800,
                easing = { OvershootInterpolator(8f).getInterpolation(it) }
            )
        )
        delay(2000L)

        val isUserLoggedIn = !FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
        Log.d("FinanceSplashScreen", "isUserLoggedIn: $isUserLoggedIn")

        if (!isConnectedToWifi(context)) {
            if (!isUserLoggedIn) {
                navController.navigate(AppScreens.LoginScreen.name)
            } else {
                navController.navigate(AppScreens.HomeScreen.name)
            }
            return@LaunchedEffect
        }
        if (!isUserLoggedIn) {
            navController.navigate(AppScreens.LoginScreen.name)
            return@LaunchedEffect
        }

    }

    var cloudTransactions = emptyList<Transaction>()
    var cloudAccount = AccountCashBalance()
    val localTransactions = viewModel.localAccountTransactions
    val localAccountCashBalance = viewModel.localAccountCashBalance
    val localAccount = viewModel.localAccount

    LaunchedEffect(
        viewModel.accountTransactions.value.loading,
        viewModel.cloudAccount.value.loading
    ) {
        val isUserLoggedIn = !FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
        if (isUserLoggedIn) {
            if (!viewModel.accountTransactions.value.data.isNullOrEmpty()) {
                cloudTransactions = viewModel.accountTransactions.value.data!!.toList()
            }
            if (viewModel.cloudAccount.value.data != null) {
                cloudAccount = viewModel.cloudAccount.value.data!!
            }
            delay(2500L)
            if (localAccountCashBalance.value == 0.0 && localTransactions.value.isEmpty()) {
                Log.d("FinanceSplashScreen", "Logging in")
                if (viewModel.accountTransactions.value.loading == false && viewModel.cloudAccount.value.loading == false) {
                    Log.d(
                        "FinanceSplashScreen",
                        "Cloud account: ${viewModel.cloudAccount.value.data}"
                    )
                    Log.d(
                        "FinanceSplashScreen",
                        "Cloud transactions: ${viewModel.accountTransactions.value.data}"
                    )
                    delay(2500L)
                    withContext(Dispatchers.IO) {
                        viewModel.addAccountCashBalance(cloudAccount)
                        viewModel.addTransactions(cloudTransactions)
                    }
                    navController.navigate(AppScreens.HomeScreen.name)
                } else {
                }
            } else {
                if (localAccount.value.lastActivityTimestamp >= cloudAccount.lastActivityTimestamp) {
                    Log.d("FinanceSplashScreen", "Already logged in, keeping local data")
                    navController.navigate(AppScreens.HomeScreen.name)
                } else {
                    if (viewModel.accountTransactions.value.loading == false && viewModel.cloudAccount.value.loading == false) {
                        Log.d("FinanceSplashScreen", "Already logged in, keeping cloud data")
                        delay(800L)
                        withContext(Dispatchers.IO) {
                            viewModel.addAccountCashBalance(cloudAccount)
                            viewModel.addTransactions(cloudTransactions)
                        }
                        navController.navigate(AppScreens.HomeScreen.name)
                    } else {
                    }
                }
            }
        }
    }

//
//    Surface(
//        modifier = Modifier
//            .padding(15.dp)
//            .size(330.dp)
//            .scale(scale.value),
//        shape = CircleShape,
//        color = Color.White,
//        border = BorderStroke(width = 2.dp, color = Color.LightGray)
//    ) {
//
//        Column(
//            modifier = Modifier.padding(1.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            FinanceLogo()
//            Spacer(modifier = Modifier.size(15.dp))
//            Text(
//                text = "\"Track. Budget. Invest.\"",
//                style = MaterialTheme.typography.titleLarge,
//                color = Color.LightGray.copy(alpha = 0.5f)
//            )
//
//        }
//
//    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .size(300.dp)
                    .scale(scale.value)
                    .shadow(8.dp, CircleShape),
                shape = CircleShape,
                color = CardBackground,
                border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AppLogo()
                    Text(
                        text = "\"Track. Budget. Invest.\"",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Finance App",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = SoftGray,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }








}