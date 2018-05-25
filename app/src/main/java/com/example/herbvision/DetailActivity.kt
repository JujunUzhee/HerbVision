package com.example.herbvision

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior

class DetailActivity : AppCompatActivity() {

    private lateinit var previewImageView: ImageView
    private lateinit var tvPlantName: TextView
    private lateinit var tvAccuracy: TextView
    private lateinit var tvManfaatBottom: TextView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var manfaatText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initViews()
        setupBottomNavigation()

        val plantName = intent.getStringExtra("plantName") ?: "Tanaman Tidak Diketahui"
        val imageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }

        showPlantInfo(plantName)
        showPlantImage(imageUri)

        // Batasi tinggi maksimum bottom sheet (misal 60% dari tinggi layar)
        limitBottomSheetHeight()
    }

    private fun initViews() {
        previewImageView = findViewById(R.id.previewImageView)
        tvPlantName = findViewById(R.id.tv_plant_name)
        tvAccuracy = findViewById(R.id.tv_accuracy)
        tvManfaatBottom = findViewById(R.id.tv_manfaat_bottom)

        val bottomSheet: LinearLayout = findViewById(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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
        val accuracy = intent.getFloatExtra("confidence", -1f)

        if (plantName == "Tanaman Tidak Diketahui") {
            tvPlantName.text = plantName
            tvAccuracy.visibility = View.GONE
            manfaatText = "Coba unggah foto lain atau gunakan tanaman yang didukung."
            showManfaatBottomSheet()
            return
        }

        val formattedName = plantName.lowercase().replace(" ", "_")
        val nameResId = resources.getIdentifier(formattedName, "string", packageName)
        val benefitResId = resources.getIdentifier("manfaat_$formattedName", "string", packageName)

        tvPlantName.text = if (nameResId != 0) getString(nameResId) else plantName
        manfaatText = if (benefitResId != 0) getString(benefitResId) else "Manfaat belum tersedia."

        if (accuracy >= 0f) {
            tvAccuracy.visibility = View.VISIBLE
            tvAccuracy.text = "Akurasi: ${String.format("%.2f", accuracy)}%"
        } else {
            tvAccuracy.visibility = View.GONE
        }

        showManfaatBottomSheet()
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

    private fun showManfaatBottomSheet() {
        tvManfaatBottom.text = manfaatText
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun limitBottomSheetHeight() {
        val bottomSheet = findViewById<LinearLayout>(R.id.bottom_sheet)
        bottomSheet.post {
            val maxHeight = (resources.displayMetrics.heightPixels * 0.4).toInt() // max 60% layar
            bottomSheet.layoutParams.height = maxHeight
            bottomSheet.requestLayout()
        }
    }
}
