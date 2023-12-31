package com.nimrichtr.uriscan.ui.home

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nimrichtr.uriscan.data.db.User

class HomeViewModel (private val application: Application) : AndroidViewModel(application = application) {

    var userList = mutableStateListOf<User?>()
    var myDatabase: FirebaseFirestore = FirebaseFirestore.getInstance()


    init {
        getUserDetails()
    }

    fun getUserDetails() {




    }

}