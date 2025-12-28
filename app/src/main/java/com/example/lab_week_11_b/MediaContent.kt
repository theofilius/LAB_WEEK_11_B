package com.example.lab_week_11_b

import android.content.ContentValues
import android.provider.MediaStore

object MediaContent {
    fun getContentValues(fileInfo: FileInfo): ContentValues {
        return ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileInfo.name)
            put(MediaStore.MediaColumns.MIME_TYPE, if (fileInfo.isPhoto) "image/jpeg" else "video/mp4")
            put(MediaStore.MediaColumns.RELATIVE_PATH, if (fileInfo.isPhoto) "Pictures/LabWeek11" else "Movies/LabWeek11")
        }
    }
}