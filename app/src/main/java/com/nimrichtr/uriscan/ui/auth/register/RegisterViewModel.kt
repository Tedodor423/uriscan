package com.nimrichtr.uriscan.ui.auth.register

import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimrichtr.uriscan.R

import com.nimrichtr.uriscan.data.auth.AuthRepository
import com.nimrichtr.uriscan.shared.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@ViewModelFactoryDsl
class RegisterViewModel (private val context: Context, private val repository: AuthRepository) : ViewModel() {

    fun registerUser(email: String, password: String) = viewModelScope.launch {
        repository.registerUser(email = email, password = password).collectLatest { result ->
            when(result) {
                is Resource.Loading -> {
                    Toast.makeText(context, "Registruji...", Toast.LENGTH_SHORT).show()
                }

                is Resource.Success -> {
                    Toast.makeText(context, "Uživatel registrován", Toast.LENGTH_SHORT).show()
                }

                is Resource.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}