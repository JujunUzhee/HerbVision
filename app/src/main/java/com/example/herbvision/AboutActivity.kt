package com.example.herbvision

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        findViewById<View>(R.id.identifikasi_layout).setOnClickListener {
            navigateTo(MainActivity::class.java)
        }

        findViewById<View>(R.id.histori_layout).setOnClickListener {
            navigateTo(HistoryActivity::class.java)
        }

        findViewById<View>(R.id.panduan_layout).setOnClickListener {
            navigateTo(UsageActivity::class.java)
        }
    }

    private fun navigateTo(targetActivity: Class<*>) {
        val intent = Intent(this, targetActivity).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        navigateTo(MainActivity::class.java)
        super.onBackPressed()
    }
}
