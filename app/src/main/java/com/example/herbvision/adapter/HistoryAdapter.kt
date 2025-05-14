package com.example.herbvision.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.herbvision.HistoryDetailActivity
import com.example.herbvision.R
import com.example.herbvision.databinding.ItemHistoryBinding
import com.example.herbvision.model.HistoryItem
import java.io.File

class HistoryAdapter(
    private val historyList: List<HistoryItem>,
    private val onDeleteClick: (HistoryItem) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HistoryItem) {
            // Load gambar dengan Glide
            if (item.imagePath.isNotEmpty()) {
                val imageFile = File(item.imagePath)
                if (imageFile.exists()) {
                    Glide.with(binding.historyImage.context)
                        .load(imageFile)
                        .override(1024, 1024)
                        .placeholder(R.drawable.placeholder_image)
                        .into(binding.historyImage)
                } else {
                    binding.historyImage.setImageResource(R.drawable.placeholder_image)
                }
            } else {
                binding.historyImage.setImageResource(R.drawable.placeholder_image)
            }

            // Set teks
            binding.historyName.text = item.plantName
            binding.historyDate.text = item.date

            // Klik item → buka detail
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, HistoryDetailActivity::class.java).apply {
                    putExtra("plant_name", item.plantName)
                    putExtra("date", item.date)
                    putExtra("accuracy", item.accuracy)
                    putExtra("image_path", item.imagePath)
                    putExtra("manfaat", item.manfaat)
                }
                context.startActivity(intent)
            }

            // Klik tombol hapus
            binding.btnDelete.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int = historyList.size
}
