package com.nimrichtr.uriscan.ui.auth.register

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nimrichtr.uriscan.R
import com.nimrichtr.uriscan.Routes
import com.nimrichtr.uriscan.ui.auth.login.LoginViewModel

@Composable
fun RegisterScreen(userRegisterViewModel: RegisterViewModel, onNextNavigate: () -> Unit) {


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
                userRegisterViewModel.registerUser(email = emailAddress, password = password)
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
fun RegisterOutlinedText(
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

// To mask the password entry using the * character, use the following function:
private class PasswordVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            AnnotatedString("*".repeat(text.text.length)),

            /**
             * [OffsetMapping.Identity] is a predefined [OffsetMapping] that can be used for the
             * transformation that does not change the character count.
             */
            OffsetMapping.Identity
        )
    }
}