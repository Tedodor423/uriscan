package com.nimrichtr.uriscan.ui.auth.login

import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.nimrichtr.uriscan.data.auth.AuthRepository
import com.nimrichtr.uriscan.shared.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ViewModelFactoryDsl
class LoginViewModel(private val context: Context, private val repository: AuthRepository) : ViewModel() {


    fun loginUser(email: String, password: String) = viewModelScope.launch {
        repository.loginUser(email = email, password = password).collectLatest { result ->
            when(result) {
                is Resource.Loading -> {
                    Toast.makeText(context, "Přihlašuji...", Toast.LENGTH_SHORT).show()
                }

                is Resource.Success -> {
                    Toast.makeText(context, "Uživatel přihlášen", Toast.LENGTH_SHORT).show()

                }

                is Resource.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}