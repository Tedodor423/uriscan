package com.nimrichtr.uriscan.ui.camera

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimrichtr.uriscan.data.images.SaveMediaModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CameraViewModel(
    private val uploadModel: SaveMediaModel
) : ViewModel() {

    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun capturePhoto(bitmap: Bitmap) {
        viewModelScope.launch {
            updateCapturedPhotoState(bitmap)
        }
    }

    fun uploadPhoto(folder: String, context: Context, patient: String, sampleDescription: String) {
        val bitmap: Bitmap? = _state.value.capturedImage
        if (bitmap != null) {
            viewModelScope.launch {
                uploadModel.call(bitmap, folder, context, patient, sampleDescription)
                updateCapturedPhotoState(null)
            }
        }
    }

    private fun updateCapturedPhotoState(updatedPhoto: Bitmap?) {
        _state.value.capturedImage?.recycle()
        _state.value = _state.value.copy(capturedImage = updatedPhoto)
    }

    override fun onCleared() {
        _state.value.capturedImage?.recycle()
        super.onCleared()
    }
}
