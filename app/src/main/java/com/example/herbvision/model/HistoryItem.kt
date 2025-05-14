package com.example.herbvision.model

data class HistoryItem(
    val id: Int,
    val plantName: String,
    val date: String,
    val accuracy: Float,
    val imagePath: String,
    val manfaat: String
)
