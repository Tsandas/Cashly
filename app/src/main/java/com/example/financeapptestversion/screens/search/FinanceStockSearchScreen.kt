package com.example.financeapptestversion.screens.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.financeapptestversion.components.FinanceAppBar
import com.example.financeapptestversion.components.InputField
import com.example.financeapptestversion.navigation.AppScreens

@Preview
@Composable
fun FinanceSearchScreen(navController: NavController = NavController(LocalContext.current)) {

    Scaffold(
        topBar = {
            FinanceAppBar(
                title = "Search for stocks",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController
            ) {
                navController.navigate(AppScreens.StocksScreen.name)
            }
        }) {
        Surface(modifier = Modifier.padding(it)) {
            Column() {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    loading = false,
                    hint = "Search"
                )

            }

        }

    }


}

@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {
    Column() {
        val searchQuerryState = rememberSaveable {
            mutableStateOf("")
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQuerryState.value) {
            searchQuerryState.value.trim().isNotEmpty()
        }

//        InputField(
//            valueState = searchQuerryState,
//            labelId = "Search",
//            enabled = true,
//            onAction = KeyboardActions {
//                if (!valid) return@KeyboardActions
//                onSearch(searchQuerryState.value.trim())
//                searchQuerryState.value = ""
//                keyboardController?.hide()
//            }
//        )
        OutlinedTextField(
            value = searchQuerryState.value,
            onValueChange = { searchQuerryState.value = it },
            label = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            enabled = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    if (!valid) return@KeyboardActions
                    onSearch(searchQuerryState.value.trim())
                    searchQuerryState.value = ""
                    keyboardController?.hide()
                }
            )
        )


    }

}