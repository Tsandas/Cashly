package com.example.financeapptestversion.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.financeapptestversion.screens.FinanceSplashScreen
import com.example.financeapptestversion.screens.details.FinanceStockDetailsScreen
import com.example.financeapptestversion.screens.home.Home
import com.example.financeapptestversion.screens.home.HomeScreenViewModel
import com.example.financeapptestversion.screens.login.FinanceLoginScreen
import com.example.financeapptestversion.screens.search.FinanceSearchScreen
import com.example.financeapptestversion.screens.search.StockSearchViewModel
import com.example.financeapptestversion.screens.stats.FinanceStatsScreen
import com.example.financeapptestversion.screens.stocks.FinanceStocksScreen
import com.example.financeapptestversion.screens.stocks.StockScreenViewModel
import com.example.financeapptestversion.screens.update.FinanceUpdateScreen

@Composable
fun AppNavigation() {
    val navControler = rememberNavController()
    NavHost(navControler, AppScreens.SplashScreen.name) {
        composable(AppScreens.SplashScreen.name) {
            FinanceSplashScreen(navControler)
        }

        composable(AppScreens.HomeScreen.name) {
            val viewModel = hiltViewModel<HomeScreenViewModel>()
            Home(navControler, viewModel)
        }

        composable(AppScreens.StocksScreen.name) {
            val viewModel = hiltViewModel<StockScreenViewModel>()
            FinanceStocksScreen(navControler, viewModel = viewModel)
        }

        val detailName = AppScreens.DetailScreen.name
        composable(
            "$detailName/{stockSymbol}", arguments = listOf(
                navArgument("stockSymbol") {
                    type = NavType.StringType
                })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("stockSymbol").let { stockSymbol ->
                FinanceStockDetailsScreen(navControler, stockSymbol.toString())
            }
        }

        composable(AppScreens.LoginScreen.name) {
            FinanceLoginScreen(navControler)
        }

        composable(AppScreens.SearchScreen.name) {
            val viewModel = hiltViewModel<StockSearchViewModel>()
            FinanceSearchScreen(navControler, viewModel)
        }

        composable(AppScreens.StatsScreen.name) {
            val stocksViewModel = hiltViewModel<StockScreenViewModel>()
            FinanceStatsScreen(navControler, viewModel = stocksViewModel)
        }

        val updateName = AppScreens.UpdateScreen.name
        composable(
            "$updateName/{stockItemId}", arguments = listOf(navArgument("stockItemId") {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("stockItemId").let {
                FinanceUpdateScreen(navControler, stockItemId = it.toString())
            }
        }

    }


}