package com.example.herbvision

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var cameraButton: ImageView
    private lateinit var galleryButton: ImageView

    companion object {
        private const val REQUEST_CAMERA = 1
        private const val REQUEST_GALLERY = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraButton = findViewById(R.id.cameraButton)
        galleryButton = findViewById(R.id.galleryButton)

        // Tombol Kamera
        cameraButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_CAMERA)
        }

        // Tombol Galeri
        galleryButton.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, REQUEST_GALLERY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    val imageBitmap = data.extras?.get("data") as Bitmap
                    navigateToAnalysis(imageBitmap)
                }
                REQUEST_GALLERY -> {
                    val imageUri = data.data
                    if (imageUri != null) {
                        navigateToAnalysis(imageUri)  // ✅ Hanya panggil jika tidak null
                    }
                }
            }
        }
    }

    private fun navigateToAnalysis(image: Any) {
        val intent = Intent(this, AnalysisActivity::class.java)
        if (image is Bitmap) {
            intent.putExtra("imageBitmap", image)
        } else if (image is android.net.Uri) {
            intent.putExtra("imageUri", image.toString())
        }
        startActivity(intent)
    }
}