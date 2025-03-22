package com.example.herbvision

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var previewImageView: ImageView
    private lateinit var tvPlantName: TextView
    private lateinit var tvManfaat: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        previewImageView = findViewById(R.id.previewImageView)
        tvPlantName = findViewById(R.id.tv_plant_name)
        tvManfaat = findViewById(R.id.tv_manfaat)

        // Ambil data gambar dari Intent
        val imageBitmap = intent.getParcelableExtra<Bitmap>("imageBitmap")
        val imageUriString = intent.getStringExtra("imageUri")

        if (imageBitmap != null) {
            previewImageView.setImageBitmap(imageBitmap)
        } else if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            previewImageView.setImageURI(imageUri)
        }

        // Simulasi hasil klasifikasi (nanti diganti dengan model TFLite)
        val dummyResult = "Lavender"
        val dummyManfaat = "Mengurangi stres, meningkatkan relaksasi, dan meredakan sakit kepala."

        // Set hasil klasifikasi
        tvPlantName.text = "Tanaman: $dummyResult"
        tvManfaat.text = dummyManfaat
    }
}
