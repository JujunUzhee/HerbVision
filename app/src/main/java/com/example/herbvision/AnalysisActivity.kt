package com.example.herbvision

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.herbvision.tflite.Classifier

class AnalysisActivity : AppCompatActivity() {

    private lateinit var previewImageView: ImageView
    private lateinit var btnIdentifikasi: Button
    private lateinit var classifier: Classifier
    private var imageBitmap: Bitmap? = null
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        // Set click listener untuk setiap menu di bottom navigation
        findViewById<View>(R.id.identifikasi_layout).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<View>(R.id.histori_layout).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        findViewById<View>(R.id.panduan_layout).setOnClickListener {
            startActivity(Intent(this, UsageActivity::class.java))
        }

        findViewById<View>(R.id.tentang_layout).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        previewImageView = findViewById(R.id.previewImageView)
        btnIdentifikasi = findViewById(R.id.btnIdentifikasi)

        classifier = Classifier(this)

        // Ambil data dari Intent
        imageBitmap = intent.getParcelableExtra("imageBitmap")
        imageUri = intent.getParcelableExtra<Uri>("imageUri")  // Ambil sebagai Uri, bukan String

        // Tampilkan gambar yang diterima
        if (imageBitmap != null) {
            previewImageView.setImageBitmap(imageBitmap)
        } else if (imageUri != null) {
            previewImageView.setImageURI(imageUri)
            imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
        }


        // Tombol untuk identifikasi
        btnIdentifikasi.setOnClickListener {
            imageBitmap?.let { bitmap ->
                val (plantName, confidence) = classifier.classifyImage(bitmap)
                navigateToDetail(plantName, confidence)
            }
        }
    }

    private fun navigateToDetail(plantName: String, confidence: Float) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("plantName", plantName)
            putExtra("confidence", confidence)

            if (imageUri != null) {
                putExtra("imageUri", imageUri.toString())  // Kirim URI jika ada
            } else if (imageBitmap != null) {
                putExtra("imageBitmap", imageBitmap)  // Kirim Bitmap jika URI tidak ada
            }
        }
        startActivity(intent)
    }

}
