package com.nimrichtr.uriscan.ui.home

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.nimrichtr.uriscan.data.db.User

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {

    LazyColumn {
        items(homeViewModel.userList) { user ->
            if (user != null) {
                PhotifyCard(user = user)
            }
        }
    }

}


@Composable
fun PhotifyCard(user: User) {

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(7.dp), elevation = CardDefaults.cardElevation(3.dp)) {
        Column(modifier = Modifier.padding(3.dp), horizontalAlignment = Alignment.Start) {
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                    append(text = "Name: ")
                }
                append(text = user.userName)
            })
            Spacer(modifier = Modifier.height(7.dp))
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                    append(text = "Age: ")
                }
                append(text = user.userAge)
            })
            Spacer(modifier = Modifier.height(7.dp))
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                    append(text = "Occupation: ")
                }
                append(text = user.userOccupation)
            })
        }
    }

}

/*
@Composable
fun ImagePicker(onImageSelected: (Uri) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> uri?.let { onImageSelected(it) } }
    )

    Button(
        onClick = { launcher.launch("image/*") }
    ) {
        Text("Select Image")
    }

}
*/