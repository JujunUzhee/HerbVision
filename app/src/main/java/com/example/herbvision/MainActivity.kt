package com.example.herbvision

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.core.view.WindowInsetsCompat.Type
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var cameraButton: ImageView
    private lateinit var cameraImageUri: Uri
    private lateinit var galleryButton: ImageView

    companion object {
        private const val REQUEST_CAMERA = 1
        private const val REQUEST_GALLERY = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<View>(R.id.bottomNavigationLayout)

        ViewCompat.setOnApplyWindowInsetsListener(bottomNav) { view, insets ->
            val systemInsets = insets.getInsets(Type.systemBars())
            view.setPadding(0, 0, 0, systemInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        // Set click listener untuk setiap menu di bottom navigation
        findViewById<View>(R.id.histori_layout).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        findViewById<View>(R.id.panduan_layout).setOnClickListener {
            startActivity(Intent(this, UsageActivity::class.java))
        }

        findViewById<View>(R.id.tentang_layout).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        cameraButton = findViewById(R.id.cameraButton)
        galleryButton = findViewById(R.id.galleryButton)

        cameraButton.setOnClickListener {
            if (checkCameraPermission()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }

        galleryButton.setOnClickListener {
            if (checkStoragePermission()) {
                openGallery()
            } else {
                requestStoragePermission()
            }
        }

        val realtimeButton = findViewById<ImageView>(R.id.realtimeButton)
        realtimeButton.setOnClickListener {
            // Optional: Cek permission kamera dulu, biar aman
            if (checkCameraPermission()) {
                startActivity(Intent(this, RealTimeActivity::class.java))
            } else {
                requestCameraPermission()
            }
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
    }

    private fun checkStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), REQUEST_GALLERY)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_GALLERY)
        }
    }

    private fun openCamera() {
        val imageFile = File.createTempFile("herb_", ".jpg", cacheDir)
        cameraImageUri = FileProvider.getUriForFile(this, "${packageName}.provider", imageFile)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, REQUEST_GALLERY)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    if (::cameraImageUri.isInitialized) {
                        navigateToAnalysis(cameraImageUri)
                    }
                }
                REQUEST_GALLERY -> {
                    val imageUri = data?.data
                    if (imageUri != null) {
                        navigateToAnalysis(imageUri) // kirim URI saja
                    }
                }
            }
        }
    }


    private fun navigateToAnalysis(image: Any) {
        val intent = Intent(this, AnalysisActivity::class.java)
        if (image is Bitmap) {
            intent.putExtra("imageBitmap", image)
        } else if (image is Uri) {
            intent.putExtra("imageUri", image.toString())
        }
        startActivity(intent)
    }

    @Suppress("MissingSuperCall")
    override fun onBackPressed() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Keluar Aplikasi")
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setPositiveButton("Ya") { _, _ ->
                finishAffinity() // Keluar dari semua aktivitas
            }
            .setNegativeButton("Tidak", null)
            .show()
    }
}