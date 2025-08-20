package com.example.herbvision

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class UsageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usage)

        // Load gambar
        loadImage(R.drawable.tutorial4, findViewById(R.id.img_tutor1))
        loadImage(R.drawable.tutorial2, findViewById(R.id.img_tutor2))
        loadImage(R.drawable.tutorial3, findViewById(R.id.img_tutor33))
        loadImage(R.drawable.tutorial4, findViewById(R.id.img_tutor4))
        loadImage(R.drawable.tutorial5, findViewById(R.id.img_tutor5))
        loadImage(R.drawable.tutorial6, findViewById(R.id.img_tutor6))
        loadImage(R.drawable.satu, findViewById(R.id.img_satu))
        loadImage(R.drawable.dua, findViewById(R.id.img_dua))
        loadImage(R.drawable.tiga, findViewById(R.id.img_tiga))
        loadImage(R.drawable.empat, findViewById(R.id.img_empat))
        loadImage(R.drawable.lima, findViewById(R.id.img_lima))
        loadImage(R.drawable.enam, findViewById(R.id.img_enam))

        // Bottom navigation listener
        findViewById<View>(R.id.identifikasi_layout).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        findViewById<View>(R.id.histori_layout).setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        findViewById<View>(R.id.tentang_layout).setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun loadImage(drawableId: Int, imageView: ImageView) {
        Glide.with(this)
            .load(drawableId)
            .override(150, 150)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)                    
            .dontTransform()
            .into(imageView)
    }


    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}
