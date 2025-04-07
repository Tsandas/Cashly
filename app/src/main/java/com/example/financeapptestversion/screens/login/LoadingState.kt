package com.example.financeapptestversion.screens.login

import android.net.http.UrlRequest

data class LoadingState(val status: Status, val message: String? = null ) {

    companion object{
        val IDLE = LoadingState(Status.IDLE)
        val SUCCESS = LoadingState(Status.SUCCESS)
        val LOADING = LoadingState(Status.LOADING)
        val FAILED = LoadingState(Status.FAILED)
    }

    enum class Status{
        LOADING,
        SUCCESS,
        IDLE,
        FAILED
    }

}
