package com.example.herbvision

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var previewImageView: ImageView
    private lateinit var tvPlantName: TextView
    private lateinit var tvManfaat: TextView
    private lateinit var tvAccuracy: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initViews()
        setupBottomNavigation()

        val plantName = intent.getStringExtra("plantName") ?: "Tanaman Tidak Diketahui"
        val imageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }

        showPlantInfo(plantName)
        showPlantImage(imageUri)
    }

    private fun initViews() {
        previewImageView = findViewById(R.id.previewImageView)
        tvPlantName = findViewById(R.id.tv_plant_name)
        tvManfaat = findViewById(R.id.tv_manfaat)
        tvAccuracy = findViewById(R.id.tv_accuracy)

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

    private fun showPlantInfo(plantName: String) {
        if (plantName == "Tanaman Tidak Diketahui") {
            tvPlantName.text = plantName
            tvManfaat.text = "Coba unggah foto lain atau gunakan tanaman yang didukung."
            return
        }
        val accuracy = intent.getFloatExtra("confidence", -1f)
        if (accuracy >= 0f) {
            tvAccuracy.visibility = View.VISIBLE
            tvAccuracy.text = "Akurasi: ${String.format("%.2f", accuracy)}%"
        } else {
            tvAccuracy.visibility = View.GONE
        }


        val formattedName = plantName.lowercase().replace(" ", "_")
        val nameResId = resources.getIdentifier(formattedName, "string", packageName)
        val benefitResId = resources.getIdentifier("manfaat_$formattedName", "string", packageName)

        tvPlantName.text = if (nameResId != 0) getString(nameResId) else plantName
        tvManfaat.text = if (benefitResId != 0) getString(benefitResId) else "Manfaat belum tersedia."
    }



    private fun showPlantImage(uri: Uri?) {
        if (uri != null) {
            Glide.with(this)
                .load(uri)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(previewImageView)
        } else {
            previewImageView.setImageResource(R.drawable.placeholder_image)
        }
    }
}
