package com.example.financeapptestversion.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.financeapptestversion.data.DataOrException
import com.example.financeapptestversion.model.MStockItem
import com.example.financeapptestversion.repository.StockRepository
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class StockSearchViewModel @Inject constructor(private val repository: StockRepository): ViewModel() {

    //Symbol Limited to AAPL, TSLA, AMZN, MSFT, NVDA, GOOGL, META, NFLX, JPM, V, BAC, AMD, PYPL, DIS, T, PFE, COST,
    // INTC, KO, TGT, NKE, SPY, BA, BABA, XOM, WMT, GE, CSCO, VZ, JNJ, CVX, PLTR, SQ, SHOP, SBUX, SOFI, HOOD, RBLX,
    // SNAP, AMD, UBER, FDX, ABBV, ETSY, MRNA, LMT, GM, F, RIVN, LCID, CCL, DAL, UAL, AAL, TSM, SONY, ET, NOK, MRO,
    // COIN, RIVN, SIRI, SOFI, RIOT, CPRX, PYPL, TGT, VWO, SPYG

    var availableStocks: List<String> = listOf(
        "AAPL",
        "TSLA",
        "AMZN",
        "MSFT",
        "NVDA",
        "GOOGL",
        "META",
        "NFLX",
        "JPM",
        "V",
        "BAC",
        "AMD",
        "PYPL",
        "DIS",
        "T",
        "PFE",
        "COST",
        "INTC",
        "KO",
        "TGT",
        "NKE",
        "SPY",
        "BA",
        "BABA",
        "XOM",
        "WMT",
        "GE",
        "CSCO",
        "VZ",
        "JNJ",
        "CVX",
        "PLTR",
        "SQ",
        "SHOP",
        "SBUX",
        "SOFI",
        "HOOD",
        "RBLX",
        "SNAP",
        "AMD",
        "UBER",
        "FDX",
        "ABBV",
        "ETSY",
        "MRNA",
        "LMT",
        "GM",
        "F",
        "RIVN",
        "LCID",
        "CCL",
        "DAL",
        "UAL",
        "AAL",
        "TSM",
        "SONY",
        "ET",
        "NOK",
        "MRO",
        "COIN",
        "RIVN",
        "SIRI",
        "SOFI",
        "RIOT",
        "CPRX",
        "PYPL",
        "TGT",
        "VWO",
        "SPYG"
    )

    var stock: MutableState<DataOrException<List<MStockItem>, Boolean, Exception>> =
        mutableStateOf(DataOrException(null, true, Exception("")))

    init {
        searchStocks("MSFT")
    }

    fun searchStocks(query: String) {

        viewModelScope.launch(Dispatchers.IO) {

            if(query.isEmpty()){
                return@launch
            }

            //stock.value.loading = true
            stock.value = repository.getStock(query)

            Log.d("TAG", "searchStocks: ${query} ${stock.value.data.toString()}")

            if (stock.value.data.toString().isNotEmpty()) stock.value.loading = false


        }

    }

}