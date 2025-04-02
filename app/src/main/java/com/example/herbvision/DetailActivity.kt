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

        val plantName = intent.getStringExtra("plantName") ?: "Tanaman Tidak Diketahui"
        val imageBitmap: Bitmap? = intent.getParcelableExtra("imageBitmap")
        val imageUriString: String? = intent.getStringExtra("imageUri")

        // Set Nama Tanaman & Manfaat
        if (plantName == "Tanaman Tidak Diketahui") {
            tvPlantName.text = "Tanaman Tidak Diketahui"
            tvManfaat.text = "Coba unggah foto lain atau gunakan tanaman yang didukung."
        } else {
            val plantResId = resources.getIdentifier(plantName.lowercase().replace(" ", "_"), "string", packageName)
            val manfaatResId = resources.getIdentifier("manfaat_${plantName.lowercase().replace(" ", "_")}", "string", packageName)

            tvPlantName.text = if (plantResId != 0) getString(plantResId) else plantName
            tvManfaat.text = if (manfaatResId != 0) getString(manfaatResId) else "Manfaat belum tersedia"
        }

        // Tampilkan gambar
        if (imageBitmap != null) {
            previewImageView.setImageBitmap(imageBitmap)
        } else if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            previewImageView.setImageURI(imageUri)
        } else {
            previewImageView.setImageResource(R.drawable.placeholder_image) // Gambar default jika tidak ada
        }
    }
}
