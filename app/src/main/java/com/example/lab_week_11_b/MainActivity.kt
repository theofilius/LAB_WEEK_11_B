package com.example.lab_week_11_b

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var fileInfo: FileInfo? = null

    private val takePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            saveToMediaStore()
        }
    }

    private val takeVideoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            saveToMediaStore()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button_take_photo).setOnClickListener {
            if (checkPermissions()) {
                launchCamera(isPhoto = true)
            }
        }

        findViewById<Button>(R.id.button_take_video).setOnClickListener {
            if (checkPermissions()) {
                launchCamera(isPhoto = false)
            }
        }
    }

    private fun launchCamera(isPhoto: Boolean) {
        val intent = Intent(
            if (isPhoto) MediaStore.ACTION_IMAGE_CAPTURE
            else MediaStore.ACTION_VIDEO_CAPTURE
        )

        // 1. Gunakan FileHelper untuk membuat file temporary
        val file = FileHelper.createTempFile(this, isPhoto)

        // 2. Dapatkan URI menggunakan FileProvider
        val uri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            file
        )

        // 3. Simpan detail ke dalam data class FileInfo
        fileInfo = FileInfo(
            name = file.name,
            path = file.absolutePath,
            uri = uri,
            isPhoto = isPhoto
        )

        // 4. Kirim URI ke intent kamera agar hasil simpan masuk ke file tersebut
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

        if (isPhoto) {
            takePhotoLauncher.launch(intent)
        } else {
            takeVideoLauncher.launch(intent)
        }
    }

    private fun saveToMediaStore() {
        fileInfo?.let { info ->
            // Menjalankan proses simpan di background menggunakan Coroutine
            lifecycleScope.launch {
                try {
                    ProviderManager.saveToMediaStore(this@MainActivity, info)
                    Toast.makeText(
                        this@MainActivity,
                        "Saved to Gallery: ${info.name}",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to save: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun checkPermissions(): Boolean {
        val permissions = arrayOf(Manifest.permission.CAMERA)
        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isNotEmpty()) {
            requestPermissions(notGranted.toTypedArray(), 101)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}