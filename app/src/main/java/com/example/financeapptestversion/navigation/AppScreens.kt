package com.example.financeapptestversion.navigation

enum class AppScreens {

    SplashScreen, LoginScreen, CreateAccountScreen, HomeScreen, SearchScreen, DetailScreen, UpdateScreen, StatsScreen, StocksScreen, AboutScreen;

    companion object {
        fun fromRoute(route: String?): AppScreens = when (route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            CreateAccountScreen.name -> CreateAccountScreen
            HomeScreen.name -> HomeScreen
            SearchScreen.name -> SearchScreen
            DetailScreen.name -> DetailScreen
            UpdateScreen.name -> UpdateScreen
            StatsScreen.name -> StatsScreen
            StocksScreen.name -> StocksScreen
            AboutScreen.name -> AboutScreen

            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }

}