@file:OptIn(ExperimentalPermissionsApi::class)

package com.nimrichtr.uriscan.ui.camera

import android.Manifest

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.nimrichtr.uriscan.ui.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CaptureScreen(
    cameraViewModel: CameraViewModel,
    onNextNavigation: () -> Unit
) {

    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    MainContent(
        hasPermission = cameraPermissionState.status.isGranted,
        onRequestPermission = cameraPermissionState::launchPermissionRequest,
        cameraViewModel = cameraViewModel,
        onNextNavigation = onNextNavigation
    )
}

@Composable
private fun MainContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    cameraViewModel: CameraViewModel,
    onNextNavigation: () -> Unit
) {

    if (hasPermission) {
        CameraScreen(
            cameraViewModel = cameraViewModel,
            onNextNavigation = onNextNavigation
        )
    } else {
        NoPermissionScreen(onRequestPermission)
    }
}

@Preview
@Composable
private fun Preview_MainContent() {
    MainContent(
        hasPermission = true,
        onRequestPermission = {},
        cameraViewModel = koinViewModel(),
        onNextNavigation = {}
    )
}