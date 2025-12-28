package com.example.lab_week_11_b

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileHelper {
    fun createTempFile(context: Context, isPhoto: Boolean): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val prefix = if (isPhoto) "JPEG_${timeStamp}_" else "VIDEO_${timeStamp}_"
        val suffix = if (isPhoto) ".jpg" else ".mp4"
        val directory = if (isPhoto) context.getExternalFilesDir("Pictures") else context.getExternalFilesDir("Movies")
        return File.createTempFile(prefix, suffix, directory)
    }
}