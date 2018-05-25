package com.example.herbvision

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.herbvision.tflite.Classifier
import com.example.herbvision.utils.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class AnalysisActivity : AppCompatActivity() {

    private lateinit var previewImageView: ImageView
    private lateinit var btnIdentifikasi: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var classifier: Classifier

    private var imageBitmap: Bitmap? = null
    private var processedBitmap: Bitmap? = null
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        initViews()
        setupBottomNavigation()
        initClassifier()
        receiveImageData()
        setupButtonClick()
    }

    private fun initViews() {
        previewImageView = findViewById(R.id.previewImageView)
        btnIdentifikasi = findViewById(R.id.btnIdentifikasi)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupBottomNavigation() {
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
    }

    private fun initClassifier() {
        classifier = Classifier(this)
    }

    private fun receiveImageData() {
        imageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }

        if (imageUri != null) {
            btnIdentifikasi.isEnabled = false
            progressBar.visibility = View.VISIBLE

            Glide.with(this)
                .load(imageUri)
                .override(1024, 1024)
                .placeholder(R.drawable.placeholder_image)
                .into(previewImageView)

            lifecycleScope.launch {
                imageBitmap = loadImageFromUri(imageUri!!)
                processedBitmap = imageBitmap?.let { processImageForModel(it) }

                progressBar.visibility = View.GONE
                btnIdentifikasi.isEnabled = processedBitmap != null

                if (imageBitmap == null) {
                    Toast.makeText(this@AnalysisActivity, "Gagal memuat gambar.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Gambar tidak ditemukan.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupButtonClick() {
        btnIdentifikasi.setOnClickListener {
            processedBitmap?.let { bitmap ->
                try {
                    val (plantName, confidence) = classifier.classifyImage(bitmap)

                    val dateFormat = java.text.SimpleDateFormat("dd MMMM yyyy", java.util.Locale("id"))
                    val currentDate = dateFormat.format(java.util.Date())

                    val dbHelper = DatabaseHelper(this)
                    val imagePath = saveImageToInternalStorage(bitmap)

                    // Gunakan 0f jika confidence null
                    val finalConfidence = confidence ?: 0f

                    dbHelper.insertHistory(this, plantName, currentDate, finalConfidence, imagePath)

                    // Tetap navigasi ke halaman detail meskipun confidence null
                    navigateToDetail(plantName, confidence)
                } catch (e: Exception) {
                    Toast.makeText(this, "Terjadi kesalahan saat klasifikasi.", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            } ?: Toast.makeText(this, "Gambar belum siap!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun cropToSquare(bitmap: Bitmap): Bitmap {
        val size = minOf(bitmap.width, bitmap.height)
        val x = (bitmap.width - size) / 2
        val y = (bitmap.height - size) / 2
        return Bitmap.createBitmap(bitmap, x, y, size, size)
    }

    private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        val safeBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && bitmap.config == Bitmap.Config.HARDWARE) {
            bitmap.copy(Bitmap.Config.ARGB_8888, true)
        } else {
            bitmap
        }

        val scaled = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(scaled)
        val paint = android.graphics.Paint().apply { isFilterBitmap = true }
        val srcRect = android.graphics.Rect(0, 0, safeBitmap.width, safeBitmap.height)
        val dstRect = android.graphics.Rect(0, 0, width, height)
        canvas.drawBitmap(safeBitmap, srcRect, dstRect, paint)
        return scaled
    }

    private fun processImageForModel(bitmap: Bitmap): Bitmap {
        val square = cropToSquare(bitmap)
        return resizeBitmap(square, 224, 224)
    }

    private suspend fun loadImageFromUri(uri: Uri): Bitmap? = withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        return try {
            val filename = "herb_${System.currentTimeMillis()}.jpg"
            val file = File(filesDir, filename)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun navigateToDetail(plantName: String, confidence: Float?) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("plantName", plantName)
            putExtra("confidence", confidence?.times(100) ?: -1f) // -1 sebagai indikator "tidak diketahui"
            putExtra("imageUri", imageUri?.toString())
        }
        startActivity(intent)
    }

}
