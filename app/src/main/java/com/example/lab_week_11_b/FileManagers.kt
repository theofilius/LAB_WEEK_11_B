package com.example.lab_week_11_b

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileManagers {
    fun createTempFile(context: Context, isPhoto: Boolean): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = if (isPhoto) "JPEG_${timeStamp}_" else "VIDEO_${timeStamp}_"
        val extension = if (isPhoto) ".jpg" else ".mp4"
        val storageDir = if (isPhoto) context.getExternalFilesDir("Pictures") else context.getExternalFilesDir("Movies")

        return File.createTempFile(fileName, extension, storageDir)
    }
}