package com.example.financeapptestversion.screens

import android.content.Context
import android.util.Log
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.financeapptestversion.components.FinanceLogo
import com.example.financeapptestversion.model.Transaction
import com.example.financeapptestversion.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FinanceSplashScreen(
    navController: NavController,
    viewModel: SplashScreenViewModel = hiltViewModel()
) {

    var listOfTransactions = emptyList<Transaction>()
    if (!viewModel.data.value.data.isNullOrEmpty()) {
        listOfTransactions = viewModel.data.value.data!!.toList()
        Log.d("splash screen", "FinanceSplashScreen: ${listOfTransactions.toString()}")
    }

    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(viewModel.data.value.loading) {

        // Start the animation only once
        if (scale.value == 0f) {
            scale.animateTo(
                targetValue = 0.9f,
                animationSpec = tween(
                    delayMillis = 800,
                    easing = { OvershootInterpolator(8f).getInterpolation(it) }
                )
            )
        }

        // When loading becomes false, navigate
        if (viewModel.data.value.loading == false) {
            delay(500L) // optional: add delay for smoother transition
            if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
                navController.navigate(AppScreens.LoginScreen.name)
            } else {
                viewModel.addTransactions(listOfTransactions)
                navController.navigate(AppScreens.HomeScreen.name)
            }
        }
//        scale.animateTo(
//            targetValue = 0.9f,
//            animationSpec = tween(
//                delayMillis = 800,
//                easing = {
//                    OvershootInterpolator(8f).getInterpolation(it)
//                })
//        )
//
//        delay(2000L)
//
//        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
//            navController.navigate(AppScreens.LoginScreen.name)
//        } else {
//            navController.navigate(AppScreens.HomeScreen.name) //make it home later
//        }

    }

    Surface(
        modifier = Modifier
            .padding(15.dp)
            .size(330.dp)
            .scale(scale.value),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(width = 2.dp, color = Color.LightGray)
    ) {

        Column(
            modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            FinanceLogo()

            Spacer(modifier = Modifier.size(15.dp))

            Text(
                text = "\"Track. Budget. Invest.\"", style = MaterialTheme.typography.titleLarge,
                color = Color.LightGray.copy(alpha = 0.5f)
            )

        }

    }

}