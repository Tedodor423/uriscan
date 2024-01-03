package com.nimrichtr.uriscan.data.images

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storageMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date

@ViewModelFactoryDsl
class UploadModel(
) {

    suspend fun call(bitmap: Bitmap, folder: String, context: Context, patient: String, sampleDescription: String) = withContext(Dispatchers.IO) {

        /*
         * Upload code from https://firebase.google.com/docs/storage/android/upload-files
         */
        Log.d("UPLOAD", "starting upload")


        val storageRef = FirebaseStorage.getInstance().reference
        val sdf = SimpleDateFormat("yyyy-M-dd_hh-mm-ss")
        val date = sdf.format(Date())
        val imageRef = storageRef.child("images/$folder/scan_$date.jpg")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()


        val uploadTask = imageRef.putBytes(data)

        uploadTask.addOnSuccessListener {
            // Image upload successful
            Toast.makeText(context, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener { e ->
            // Image upload failed
            Toast.makeText(context, "Image upload failed: $e", Toast.LENGTH_SHORT).show()
        }

        // Update metadata
        val metadata = storageMetadata {
            contentType = "image/jpg"
            setCustomMetadata("patient", patient)
            setCustomMetadata("description", sampleDescription)

        }

        imageRef.updateMetadata(metadata).addOnSuccessListener { updatedMetadata ->
            // Updated metadata is in updatedMetadata
        }.addOnFailureListener {
            // Uh-oh, an error occurred!
        }


        // saving images on device
        /*
        val resolver: ContentResolver = context.applicationContext.contentResolver

        val imageCollection: Uri = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            else -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        // Publish a new image.
        val nowTimestamp: Long = System.currentTimeMillis()
        val imageContentValues: ContentValues = ContentValues().apply {

            put(MediaStore.Images.Media.DISPLAY_NAME, "Your image name" + ".jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.DATE_TAKEN, nowTimestamp)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/YourAppNameOrAnyOtherSubFolderName")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                put(MediaStore.Images.Media.DATE_TAKEN, nowTimestamp)
                put(MediaStore.Images.Media.DATE_ADDED, nowTimestamp)
                put(MediaStore.Images.Media.DATE_MODIFIED, nowTimestamp)
                put(MediaStore.Images.Media.AUTHOR, "Your Name")
                put(MediaStore.Images.Media.DESCRIPTION, "Your description")
            }
        }

        val imageMediaStoreUri: Uri? = resolver.insert(imageCollection, imageContentValues)

        // Write the image data to the new Uri.
        val result: Result<Unit> = imageMediaStoreUri?.let { uri ->
            kotlin.runCatching {
                resolver.openOutputStream(uri).use { outputStream: OutputStream? ->
                    checkNotNull(outputStream) { "Couldn't create file for gallery, MediaStore output stream is null" }
                    capturePhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    imageContentValues.clear()
                    imageContentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(uri, imageContentValues, null, null)
                }

                Result.success(Unit)
            }.getOrElse { exception: Throwable ->
                exception.message?.let(::println)
                resolver.delete(uri, null, null)
                Result.failure(exception)
            }
        } ?: run {
            Result.failure(Exception("Couldn't create file for gallery"))
        }
        */
    }
}
