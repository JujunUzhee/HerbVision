package com.example.herbvision

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class AnalysisActivity : AppCompatActivity() {

    private lateinit var previewImageView: ImageView
    private lateinit var btnIdentifikasi: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        previewImageView = findViewById(R.id.previewImageView)
        btnIdentifikasi = findViewById(R.id.btnIdentifikasi)

        // Ambil data gambar dari Intent
        val imageBitmap = intent.getParcelableExtra<Bitmap>("imageBitmap")
        val imageUriString = intent.getStringExtra("imageUri")

        if (imageBitmap != null) {
            previewImageView.setImageBitmap(imageBitmap)
        } else if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            previewImageView.setImageURI(imageUri)
        }

        // Tombol Identifikasi
        btnIdentifikasi.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            if (imageBitmap != null) {
                intent.putExtra("imageBitmap", imageBitmap)
            } else if (imageUriString != null) {
                intent.putExtra("imageUri", imageUriString)
            }
            startActivity(intent)
        }
    }
}
