package com.nimrichtr.uriscan.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimrichtr.uriscan.data.images.UploadModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CameraViewModel(
    private val uploadModel: UploadModel = UploadModel()
) : ViewModel() {



    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun saveImage(bitmap: Bitmap) {
        viewModelScope.launch {
            Log.d("UPLOAD", "calling updateCaptured Photo")
            updateCapturedPhotoState(bitmap)
        }
    }

    fun clearImage() {
        updateCapturedPhotoState(null)
    }

    fun getImage(): Bitmap? {
        return _state.value.capturedImage
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
