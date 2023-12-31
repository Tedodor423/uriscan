package com.nimrichtr.uriscan.ui.upload

import android.app.Application
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nimrichtr.uriscan.R
import com.nimrichtr.uriscan.ui.camera.CameraViewModel
import com.nimrichtr.uriscan.ui.home.HomeViewModel


@Composable
fun UploadScreen(context: Context,
                 onNavigateNext: () -> Unit,
                 uploadViewModel: UploadViewModel = UploadViewModel(),
                 homeViewModel: HomeViewModel,
                 cameraViewModel: CameraViewModel,
                 onCancelButtonClicked: () -> Unit
) {


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        //Image(modifier = Modifier.fillMaxSize(), painter = painterResource(id = R.drawable.register), contentDescription = "Register")
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AddNewUserOutlinedText(value = uploadViewModel.patient, onValueChanged = { uploadViewModel.patient = it }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next), label = {
                Text(
                    text = stringResource(id = R.string.patient_name)
                )
            })
            Spacer(modifier = Modifier.height(height = 7.dp))
            AddNewUserOutlinedText(value = uploadViewModel.sampleDescription, onValueChanged = { uploadViewModel.sampleDescription = it }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next), label = {
                Text(
                    text = "Age"
                )
            })

            Spacer(modifier = Modifier.height(height = 21.dp))
            Button(onClick = {
                cameraViewModel.uploadPhoto("samples", context, uploadViewModel.patient, uploadViewModel.sampleDescription)
                onNavigateNext()
            }) {
                Text(text = "Add")
            }
            OutlinedButton(modifier = Modifier.weight(1f), onClick = onCancelButtonClicked) {
                Text(stringResource(R.string.cancel))
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewUserOutlinedText(
    value: String,
    onValueChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    label: @Composable (() -> Unit)?
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        keyboardOptions = keyboardOptions,
        label = label
    )
}