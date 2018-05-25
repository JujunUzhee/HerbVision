package com.example.herbvision

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.herbvision.databinding.ActivityHistoryDetailBinding

class HistoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayHistoryDetails()
    }

    private fun displayHistoryDetails() {
        val plantName = intent.getStringExtra("plant_name") ?: "Tanaman Tidak Diketahui"
        val date = intent.getStringExtra("date") ?: "-"
        val accuracy = intent.getFloatExtra("accuracy", 0f)
        val imagePath = intent.getStringExtra("image_path")
        val manfaat = intent.getStringExtra("manfaat") ?: "Manfaat tidak tersedia"

        binding.detailPlantName.text = plantName
        binding.detailDate.text = date
        binding.detailManfaat.text = manfaat

        if (plantName == "Tanaman Tidak Diketahui") {
            binding.detailAccuracy.text = "" // kosongkan akurasi
            binding.detailAccuracy.visibility = android.view.View.GONE // sembunyikan view
        } else {
            binding.detailAccuracy.text = "Akurasi: ${String.format("%.2f", accuracy)}%"
            binding.detailAccuracy.visibility = android.view.View.VISIBLE
        }

        if (!imagePath.isNullOrEmpty()) {
            val uri = Uri.parse(imagePath)
            binding.detailImage.setImageURI(uri)
        } else {
            binding.detailImage.setImageResource(R.drawable.placeholder_image)
        }
    }

}
