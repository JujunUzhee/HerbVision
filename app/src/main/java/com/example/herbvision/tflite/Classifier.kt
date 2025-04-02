package com.example.herbvision.tflite

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.nio.MappedByteBuffer
import java.nio.ByteBuffer

class Classifier(context: Context) {

    private var interpreter: Interpreter
    private val inputImageSize = 224  // Sesuai dengan ukuran input model
    private val numClasses = 10       // Sesuai dengan jumlah kelas tanaman

    init {
        val model: MappedByteBuffer = FileUtil.loadMappedFile(context, "model_cnn_resnet50.tflite")
        interpreter = Interpreter(model)
    }

    fun classifyImage(bitmap: Bitmap): Pair<String, Float> {
        // Proses gambar sebelum masuk ke model
        val tensorImage = preprocessImage(bitmap)

        // Buffer hasil prediksi
        val outputArray = Array(1) { FloatArray(numClasses) }

        // Jalankan model
        interpreter.run(tensorImage.buffer, outputArray)

        // Cari hasil dengan probabilitas tertinggi
        val maxIndex = outputArray[0].indices.maxByOrNull { outputArray[0][it] } ?: -1
        val confidence = outputArray[0][maxIndex]

        val labels = listOf("Lavender", "Rosemary", "Tapak Dara", "Sirih", "Sage", "Jarak Tintir", "Mint", "Kelor", "Temulawak", "Lidah Buaya")

        // Cek jika prediksi valid
        if (maxIndex !in labels.indices) {
            Log.e("Classifier", "Kesalahan prediksi, index keluar batas: $maxIndex")
            return Pair("Tanaman Tidak Diketahui", 0.0f)
        }

        val predictedLabel = labels[maxIndex]

        // Logging untuk debug
        Log.d("Classifier", "Prediksi: $predictedLabel, Confidence: $confidence")

        return Pair(predictedLabel, confidence)
    }

    private fun preprocessImage(bitmap: Bitmap): TensorImage {
        val tensorImage = TensorImage(DataType.FLOAT32)

        // Proses gambar: Resize dan normalisasi otomatis oleh TensorFlow Lite
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(inputImageSize, inputImageSize, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f)) // Normalisasi dari 0-255 ke 0-1
            .build()

        tensorImage.load(bitmap)
        return imageProcessor.process(tensorImage)
    }



}
