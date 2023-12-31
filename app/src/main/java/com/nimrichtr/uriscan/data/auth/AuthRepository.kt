package com.nimrichtr.uriscan.data.auth

import com.google.firebase.auth.AuthResult
import com.nimrichtr.uriscan.shared.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>

    fun registerUser(email: String, password: String): Flow<Resource<AuthResult>>

}