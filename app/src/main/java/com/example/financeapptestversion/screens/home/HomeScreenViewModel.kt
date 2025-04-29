package com.example.financeapptestversion.screens.home

import androidx.lifecycle.ViewModel
import com.example.financeapptestversion.repository.AccountTransactionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: AccountTransactionsRepository) :
    ViewModel() {



}