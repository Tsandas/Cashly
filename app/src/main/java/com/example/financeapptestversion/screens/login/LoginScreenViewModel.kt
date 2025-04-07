package com.example.financeapptestversion.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapptestversion.model.MUser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {

    //val loadingState = MutableStateFlow(LoadingState.IDLE)
    val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading


    fun createUserWithEmailAndPassword(email: String, password: String, home: () -> Unit) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val displayName = task.result?.user?.email?.split('@')?.get(0)
                        createUser(displayName)
                        home()
                    } else {
                        Log.d("FB", "createUserWithEmailAndPassword: ${task.result.toString()}")
                    }
                    _loading.value = false
                }
        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val user = MUser(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "Life is great",
            profession = "Android Developer",
            id = null
        ).toMap()
        user["user_id"] = userId.toString()
        user["display_name"] = displayName.toString()
        FirebaseFirestore.getInstance().collection("users").add(user)

    }



    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(
                                "FB",
                                "createUserWithEmailAndPassword: Fuck yea ${task.result.toString()}"
                            )
                            //Todo: ("take users home")
                            home()
                        } else {
                            Log.d("FB", "createUserWithEmailAndPassword: ${task.result.toString()}")
                        }
                    }
            } catch (ex: Exception) {
                Log.d("FB", "createUserWithEmailAndPassword: ${ex.message}")
            }
        }


}