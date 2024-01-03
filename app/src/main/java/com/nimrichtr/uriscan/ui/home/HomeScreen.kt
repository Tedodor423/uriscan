package com.nimrichtr.uriscan.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nimrichtr.uriscan.R


@Composable
fun HomeScreen(homeViewModel: HomeViewModel,
               onScanNavigate: () -> Unit,
               onRegisterNavigate: () -> Unit,
               onLoginNavigate: () -> Unit,
               ) {


    Box (
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(id = R.string.current_user).plus(homeViewModel.userName))

            if (homeViewModel.loggedIn) {
                Button(onClick = {
                    onScanNavigate()
                }) {
                    Text(
                        stringResource(id = R.string.upload_scan_button)
                    )
                }

            } else {
                Button(onClick = {
                    onLoginNavigate()
                }) {
                    Text(
                        stringResource(id = R.string.Login)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    onRegisterNavigate()
                }) {
                    Text(
                        stringResource(id = R.string.Register)
                    )
                }

            }
        }
    }

}


/*@Composable
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

}*/

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