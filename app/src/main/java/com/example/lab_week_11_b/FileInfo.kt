package com.example.lab_week_11_b

import android.net.Uri

data class FileInfo(
    val name: String,
    val path: String,
    val uri: Uri,
    val isPhoto: Boolean
)