package com.example.herbvision

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class UsageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_usage)

        // Load gambar menggunakan Glide (dengan optimalisasi)
        loadImage(R.drawable.tutor1, findViewById(R.id.img_tutor1))
        loadImage(R.drawable.tutor2, findViewById(R.id.img_tutor2))
        loadImage(R.drawable.tutor3, findViewById(R.id.img_tutor3))
        loadImage(R.drawable.satu, findViewById(R.id.img_satu))
        loadImage(R.drawable.dua, findViewById(R.id.img_dua))
        loadImage(R.drawable.tiga, findViewById(R.id.img_tiga))

        // Bottom navigation listener
        findViewById<View>(R.id.identifikasi_layout).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<View>(R.id.histori_layout).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        findViewById<View>(R.id.tentang_layout).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        // Menyesuaikan padding dengan insets
        val panduanView = findViewById<View>(R.id.panduan_layout)
        if (panduanView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(panduanView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }

    // Fungsi untuk memuat gambar dengan Glide
    private fun loadImage(drawableId: Int, imageView: ImageView) {
        Glide.with(this)
            .load(drawableId)
            .override(150, 150) // Sesuaikan ukuran agar tidak blur
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache untuk mengurangi reload gambar
            .dontTransform() // Hindari transformasi otomatis agar tidak blur
            .into(imageView)
    }
}
