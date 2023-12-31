/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nimrichtr.uriscan

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nimrichtr.uriscan.data.auth.AuthRepository
import com.nimrichtr.uriscan.data.auth.AuthRepositoryImpl
import com.nimrichtr.uriscan.ui.auth.login.LoginScreen
import com.nimrichtr.uriscan.ui.auth.login.LoginViewModel
import com.nimrichtr.uriscan.ui.auth.register.RegisterScreen
import com.nimrichtr.uriscan.ui.auth.register.RegisterViewModel
import com.nimrichtr.uriscan.ui.camera.CameraViewModel
import com.nimrichtr.uriscan.ui.camera.CaptureScreen
import com.nimrichtr.uriscan.ui.home.HomeScreen
import com.nimrichtr.uriscan.ui.upload.UploadScreen
import com.nimrichtr.uriscan.ui.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

enum class Routes(@StringRes val title: Int) {
    Register(title = R.string.register_page_name),
    Login(title = R.string.login_page_name),
    Home(title = R.string.app_name),
    Scan(title = R.string.scan_page_name),
    Upload(title = R.string.upload_page_name)
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CupcakeAppBar(
    currentScreen: Routes,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun UriScanApp(
    homeViewModel: HomeViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    cameraViewModel: CameraViewModel = koinViewModel(),
    context: Context = LocalContext.current,
    firebaseAuth: FirebaseAuth = Firebase.auth


) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = Routes.valueOf(
        backStackEntry?.destination?.route ?: Routes.Home.name
    )

    Scaffold(
        topBar = {
            CupcakeAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Routes.Register.name) {
                RegisterScreen(
                    userRegisterViewModel = RegisterViewModel(context, AuthRepositoryImpl(firebaseAuth)),
                    onNextNavigate = {navController.navigate(Routes.Home.name)}
                )
            }

            composable(route = Routes.Login.name) {
                LoginScreen(
                    userLoginViewModel = LoginViewModel(context, AuthRepositoryImpl(firebaseAuth)),
                    onNextNavigate = {navController.navigate(Routes.Home.name)}
                )
            }

            composable(route = Routes.Home.name) {
                HomeScreen()
            }

            composable(route = Routes.Scan.name) {
                CaptureScreen(
                    cameraViewModel = cameraViewModel,
                    onNextNavigation = {navController.navigate(Routes.Upload.name)}
                )
            }

            composable(route = Routes.Upload.name) {
                val context = LocalContext.current
                UploadScreen(
                    context = context,
                    onCancelButtonClicked = {navController.popBackStack(Routes.Home.name, inclusive = false)},
                    onNavigateNext = {navController.navigate(Routes.Home.name)},
                    cameraViewModel = cameraViewModel,
                    homeViewModel = homeViewModel,
                )
            }
        }
    }
}

/**
 * Creates an intent to share order details
 */
private fun shareOrder(context: Context, subject: String, summary: String) {
    // Create an ACTION_SEND implicit intent with order details in the intent extras
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.new_cupcake_order)
        )
    )
}
