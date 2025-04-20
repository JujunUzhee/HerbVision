package com.example.herbvision.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.herbvision.model.HistoryItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object HistoryManager {

    private const val PREFS_NAME = "history_prefs"
    private const val HISTORY_KEY = "history_key"

    fun getHistory(context: Context): List<HistoryItem> {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(HISTORY_KEY, "[]")
        val type = object : TypeToken<List<HistoryItem>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun saveHistory(context: Context, history: List<HistoryItem>) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(history)
        editor.putString(HISTORY_KEY, json)
        editor.apply()
    }
}
