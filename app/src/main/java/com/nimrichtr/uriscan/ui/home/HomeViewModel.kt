package com.nimrichtr.uriscan.ui.home

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class HomeViewModel (private val application: Application) : AndroidViewModel(application = application) {

    var currentUser: FirebaseUser? = null
    var loggedIn = false
    var userName: String? = ""

    fun updateUser(){
        loggedIn = currentUser != null
        if (loggedIn) {
            userName = currentUser!!.email
        } else {
            userName = "žádný"
        }
    }
}