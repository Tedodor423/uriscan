package com.nimrichtr.uriscan.ui.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.nimrichtr.uriscan.R
import com.nimrichtr.uriscan.ui.auth.register.RegisterOutlinedText

@Composable
fun LoginScreen(userLoginViewModel: LoginViewModel, onNextNavigate: () -> Unit) {


    var emailAddress by rememberSaveable { mutableStateOf("") }

    var password by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        //Image(modifier = Modifier.fillMaxSize(), painter = painterResource(id = R.drawable.register), contentDescription = stringResource(R.string.register))
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RegisterOutlinedText(value = emailAddress, onValueChanged = { emailAddress = it }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next), label = {
                Text(
                    text = stringResource(id = R.string.Email)
                )
            })
            Spacer(modifier = Modifier.height(height = 7.dp))
            RegisterOutlinedText(value = password, onValueChanged = { password = it }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next), label = {
                Text(
                    text = stringResource(id = R.string.Password)
                )
            }, applyVisualTransformation = true)
            Spacer(modifier = Modifier.height(height = 21.dp))
            Button(onClick = {
                userLoginViewModel.loginUser(email = emailAddress, password = password)
                onNextNavigate()
            }) {
                Text(
                    stringResource(id = R.string.Register)
                )
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginOutlinedText(
    value: String,
    onValueChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    label: @Composable (() -> Unit)?,
    applyVisualTransformation: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        keyboardOptions = keyboardOptions,
        label = label,
        visualTransformation = if (applyVisualTransformation) PasswordVisualTransformation() else VisualTransformation.None
    )
}