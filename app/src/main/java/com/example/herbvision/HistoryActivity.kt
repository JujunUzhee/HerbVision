package com.example.herbvision

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.herbvision.adapter.HistoryAdapter
import com.example.herbvision.databinding.ActivityHistoryBinding
import com.example.herbvision.model.HistoryItem
import com.example.herbvision.utils.DatabaseHelper

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        // Bottom navigation click listeners
        findViewById<View>(R.id.identifikasi_layout).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        findViewById<View>(R.id.panduan_layout).setOnClickListener {
            val intent = Intent(this, UsageActivity::class.java)
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

        // Load history data
        loadHistory()
    }

    private fun loadHistory() {
        val historyList = dbHelper.getAllHistory()
        val adapter = HistoryAdapter(historyList) { selectedItem ->
            val isDeleted = dbHelper.deleteHistoryById(selectedItem.id)
            if (isDeleted) {
                Toast.makeText(this, "Riwayat berhasil dihapus", Toast.LENGTH_SHORT).show()
                loadHistory() // Refresh list setelah hapus
            } else {
                Toast.makeText(this, "Gagal menghapus riwayat", Toast.LENGTH_SHORT).show()
            }
        }

        binding.rvHistori.layoutManager = LinearLayoutManager(this)
        binding.rvHistori.adapter = adapter
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}
