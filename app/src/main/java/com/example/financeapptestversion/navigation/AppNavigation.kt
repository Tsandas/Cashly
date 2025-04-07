package com.example.financeapptestversion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.financeapptestversion.screens.FinanceSplashScreen
import com.example.financeapptestversion.screens.details.FinanceStockDetailsScreen
import com.example.financeapptestversion.screens.home.Home
import com.example.financeapptestversion.screens.login.FinanceLoginScreen
import com.example.financeapptestversion.screens.search.FinanceSearchScreen
import com.example.financeapptestversion.screens.stats.FinanceStatsScreen
import com.example.financeapptestversion.screens.stocks.FinanceStocksScreen
import com.example.financeapptestversion.screens.update.FinanceUpdateScreen

@Composable
fun AppNavigation() {
    val navControler = rememberNavController()
    NavHost(navControler, AppScreens.SplashScreen.name){
        composable(AppScreens.SplashScreen.name){
            FinanceSplashScreen(navControler)
        }

        composable(AppScreens.HomeScreen.name){
            Home(navControler)
        }

        composable(AppScreens.StocksScreen.name){
            FinanceStocksScreen(navControler)
        }

        composable(AppScreens.DetailScreen.name){
            FinanceStockDetailsScreen(navControler)
        }

        composable(AppScreens.LoginScreen.name){
            FinanceLoginScreen(navControler)
        }

        composable(AppScreens.SearchScreen.name){
            FinanceSearchScreen(navControler)
        }

        composable(AppScreens.StatsScreen.name){
            FinanceStatsScreen(navControler)
        }

        composable(AppScreens.UpdateScreen.name){
            FinanceUpdateScreen(navControler)
        }

    }


}