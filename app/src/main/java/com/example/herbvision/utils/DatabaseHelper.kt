package com.example.herbvision.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.herbvision.R
import com.example.herbvision.model.HistoryItem

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "herbvision.db"
        private const val DATABASE_VERSION = 2

        const val TABLE_HISTORY = "history"
        const val COLUMN_ID = "id"
        const val COLUMN_PLANT_NAME = "plant_name"
        const val COLUMN_DATE = "date"
        const val COLUMN_ACCURACY = "accuracy"
        const val COLUMN_IMAGE_PATH = "image_path"
        const val COLUMN_MANFAAT = "manfaat"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_HISTORY (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PLANT_NAME TEXT,
                $COLUMN_DATE TEXT,
                $COLUMN_ACCURACY REAL,
                $COLUMN_IMAGE_PATH TEXT,
                $COLUMN_MANFAAT TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        onCreate(db)
    }

    fun deleteHistoryById(id: Int): Boolean {
        val db = this.writableDatabase
        val deletedRows = db.delete(TABLE_HISTORY, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return deletedRows > 0
    }
    //  insert dengan Manfaat otomatis
    fun insertHistory(context: Context, plantName: String, date: String, accuracy: Float, imagePath: String) {
        val manfaatMap = mapOf(
            "lavender" to context.getString(R.string.manfaat_lavender),
            "rosemary" to context.getString(R.string.manfaat_rosemary),
            "tapak_dara" to context.getString(R.string.manfaat_tapak_dara),
            "bidara" to context.getString(R.string.manfaat_bidara),
            "saga" to context.getString(R.string.manfaat_saga),
            "jarak_tintir" to context.getString(R.string.manfaat_jarak_tintir),
            "mint" to context.getString(R.string.manfaat_mint),
            "kelor" to context.getString(R.string.manfaat_kelor),
            "temulawak" to context.getString(R.string.manfaat_temulawak),
            "lidah_buaya" to context.getString(R.string.manfaat_lidah_buaya)
        )

        val key = plantName.lowercase().replace(" ", "_")
        val manfaat = manfaatMap[key] ?: "Manfaat belum tersedia"

        val values = ContentValues().apply {
            put(COLUMN_PLANT_NAME, plantName)
            put(COLUMN_DATE, date)
            put(COLUMN_ACCURACY, accuracy * 100)
            put(COLUMN_IMAGE_PATH, imagePath)
            put(COLUMN_MANFAAT, manfaat)
        }

        val db = this.writableDatabase
        db.insert(TABLE_HISTORY, null, values)
        db.close()
    }


    fun getAllHistory(): List<HistoryItem> {
        val historyList = mutableListOf<HistoryItem>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_HISTORY ORDER BY $COLUMN_ID DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val item = HistoryItem(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    plantName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLANT_NAME)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                    accuracy = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_ACCURACY)),
                    imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)),
                    manfaat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MANFAAT))
                )
                historyList.add(item)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return historyList
    }
}
