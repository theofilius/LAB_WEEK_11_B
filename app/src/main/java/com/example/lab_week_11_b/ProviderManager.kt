package com.example.lab_week_11_b

import android.content.Context
import android.provider.MediaStore
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream

object ProviderManager {
    fun saveToMediaStore(context: Context, fileInfo: FileInfo) {
        val contentValues = MediaContent.getContentValues(fileInfo)
        val contentUri = if (fileInfo.isPhoto)
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        else
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val uri = context.contentResolver.insert(contentUri, contentValues)

        uri?.let { targetUri ->
            context.contentResolver.openOutputStream(targetUri)?.use { outputStream ->
                FileInputStream(File(fileInfo.path)).use { inputStream ->
                    IOUtils.copy(inputStream, outputStream)
                }
            }
        }
    }
}