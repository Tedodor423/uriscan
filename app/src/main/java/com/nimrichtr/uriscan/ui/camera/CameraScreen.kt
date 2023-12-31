package com.nimrichtr.uriscan.ui.camera

import android.content.Context
import android.graphics.Bitmap

import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import java.util.concurrent.Executor


/*
Code for camera overlay from https://stackoverflow.com/a/73545721
 */

@Composable
fun CameraScreen(
    cameraViewModel: CameraViewModel,
    onNextNavigation: () -> Unit
) {
    val cameraState: CameraState by cameraViewModel.state.collectAsStateWithLifecycle()

    CameraContent(
        cameraViewModel = cameraViewModel,
        onNextNavigation = onNextNavigation

    )
}

@Composable
private fun CameraContent(
    cameraViewModel: CameraViewModel,
    onNextNavigation: () -> Unit
) {

    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController = remember { LifecycleCameraController(context) }

    val showDialog = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Take photo") },
                onClick = {
                    capturePhoto(context, cameraController, cameraViewModel::saveImage, {showDialog.value = true})

                    Log.d("UPLOAD", "clicked capture button")
                          },
                icon = { Icon(imageVector = Icons.Default.Camera, contentDescription = "Camera capture icon") }
            )
        }
    ) { paddingValues: PaddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                        setBackgroundColor(0)
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        previewView.controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                }
            )
            TransparentClipLayout(
                modifier = Modifier.fillMaxSize(),
                width = 300.dp,
                height = 200.dp,
                offsetY = 150.dp
            )

            if (showDialog.value) {
                Log.d("UPLOAD", "showing dialog ".plus(cameraViewModel.getImage().toString()))
                if (cameraViewModel.getImage() != null) {
                    ShowCroppedImageDialog(
                        imageBitmap = cameraViewModel.getImage()!!.asImageBitmap(),
                        width = 300.dp,
                        height = 200.dp,
                        offsetY = 150.dp,
                        modifier = Modifier.fillMaxSize(),
                        onCropSuccess = {cameraViewModel.saveImage(it.asAndroidBitmap()) },
                        onConfirm = {onNextNavigation()},
                        onDismissRequest = {
                            showDialog.value = false
                            cameraViewModel.clearImage()
                        } )
                } else {
                    showDialog.value=false
                }
            }

            /*
            TODO: multiple scans at once
            if (lastCapturedPhoto != null) {
                LastPhotoPreview(
                    modifier = Modifier.align(alignment = Alignment.BottomStart),
                    lastCapturedPhoto = lastCapturedPhoto
                )
            }*/
        }
    }
}

private fun capturePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
    saveImage: (Bitmap) -> Unit,
    showDialog: () -> Unit
) {
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {

        override fun onCaptureSuccess(image: ImageProxy) {
            val imageBitmap: Bitmap = image
                .toBitmap()

            Log.d("UPLOAD", "calling onPhotoCapture")
            saveImage(imageBitmap)
            image.close()
            showDialog()
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("CameraContent", "Error capturing image", exception)
        }
    })
}


@Composable
fun ShowCroppedImageDialog(imageBitmap: ImageBitmap,
                           width: Dp,
                           height: Dp,
                           offsetY: Dp,
                           modifier: Modifier,
                           onCropSuccess: (ImageBitmap) -> Unit,
                           onConfirm: () -> Unit,
                           onDismissRequest: () -> Unit
) {
    Log.d("UPLOAD", "showing crop dialog")
    val offsetInPx: Float
    val widthInPx: Float
    val heightInPx: Float
    var croppedBitmap:ImageBitmap? = null

    with(LocalDensity.current) {
        offsetInPx = offsetY.toPx()
        widthInPx = width.toPx()
        heightInPx = height.toPx()
    }

    BoxWithConstraints(modifier) {

        val composableWidth = constraints.maxWidth
        val composableHeight = constraints.maxHeight


        val widthRatio = imageBitmap.width / composableWidth.toFloat()
        val heightRatio = imageBitmap.height / composableHeight.toFloat()

        val rectDraw = remember {
            Rect(
                offset = Offset(
                    x = (composableWidth - widthInPx) / 2f,
                    y = offsetInPx
                ),
                size = Size(widthInPx, heightInPx)
            )
        }

        val rectCrop by remember {
            mutableStateOf(
                IntRect(
                    offset = IntOffset(
                        (rectDraw.left * widthRatio).toInt(),
                        (rectDraw.top * heightRatio).toInt()
                    ),
                    size = IntSize(
                        (rectDraw.width * widthRatio).toInt(),
                        (rectDraw.height * heightRatio).toInt()
                    )
                )
            )
        }
        val crop = true

        LaunchedEffect(crop) {
            if (crop) {
                Log.d("UPLOAD", "cropping")
                delay(500)
                croppedBitmap = Bitmap.createBitmap(
                    imageBitmap.asAndroidBitmap(),
                    rectCrop.left,
                    rectCrop.top,
                    rectCrop.width,
                    rectCrop.height
                ).asImageBitmap()
                val croppedBitmapTemp = croppedBitmap
                onCropSuccess(croppedBitmapTemp!!)
            }
        }
    }


    AlertDialog(
        onDismissRequest = onDismissRequest,
        text = {
            if (croppedBitmap != null)
            Image(
                modifier = Modifier.fillMaxWidth(),  //.aspectRatio(ratio = 3.toFloat()),
                contentScale = ContentScale.Fit,
                bitmap = imageBitmap,
                contentDescription = "result"
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}



@Composable
fun TransparentClipLayout(
    modifier: Modifier,
    width: Dp,
    height: Dp,
    offsetY: Dp
) {

    val offsetInPx: Float
    val widthInPx: Float
    val heightInPx: Float

    with(LocalDensity.current) {
        offsetInPx = offsetY.toPx()
        widthInPx = width.toPx()
        heightInPx = height.toPx()
    }



    Canvas(modifier = modifier) {

        val canvasWidth = size.width

        with(drawContext.canvas.nativeCanvas) {
            val checkPoint = saveLayer(null, null)

            // Destination
            drawRect(Color(0x77000000))

            // Source
            drawRoundRect(
                topLeft = Offset(
                    x = (canvasWidth - widthInPx) / 2,
                    y = offsetInPx
                ),
                size = Size(widthInPx, heightInPx),
                cornerRadius = CornerRadius(30f,30f),
                color = Color.Transparent,
                blendMode = BlendMode.Clear
            )
            restoreToCount(checkPoint)
        }

    }
}



@Composable
private fun LastPhotoPreview(
    modifier: Modifier = Modifier,
    lastCapturedPhoto: Bitmap
) {

    val capturedPhoto: ImageBitmap = remember(lastCapturedPhoto.hashCode()) { lastCapturedPhoto.asImageBitmap() }

    Card(
        modifier = modifier
            .size(128.dp)
            .padding(16.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.large
    ) {
        Image(
            bitmap = capturedPhoto,
            contentDescription = "Last captured photo",
            contentScale = ContentScale.Crop
        )
    }
}
