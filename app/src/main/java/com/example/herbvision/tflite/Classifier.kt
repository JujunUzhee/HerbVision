package com.example.herbvision.tflite

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import java.nio.MappedByteBuffer

class Classifier(context: Context) {

    private var interpreter: Interpreter
    private val inputImageSize = 224
    private val numClasses = 11
    private val CONFIDENCE_THRESHOLD = 0.5f

    private val labels = listOf( // UPDATED
        "Bidara", "Jarak Tintir", "Kelor", "Lavender", "Lidah Buaya",
        "Mint", "Rosemary", "Saga", "Tapak Dara", "Temulawak","unknown"
    )

    init {
        val model: MappedByteBuffer = FileUtil.loadMappedFile(context, "model_resnet50.tflite")
        val options = Interpreter.Options().apply {
            setNumThreads(4)
        }
        interpreter = Interpreter(model, options)
        Log.d("Classifier", "Model ResNet50 berhasil dimuat")
    }

    fun classifyImage(bitmap: Bitmap): Pair<String, Float> {
        val tensorImage = preprocessImage(bitmap)
        val outputArray = Array(1) { FloatArray(numClasses) }

        return try {
            interpreter.run(tensorImage.buffer, outputArray)
            val probabilities = outputArray[0]
            val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
            val confidence = probabilities.getOrNull(maxIndex) ?: 0f

            probabilities.forEachIndexed { index, score ->
                Log.d("Classifier", "Kelas ${labels[index]}: $score")
            }

            val top3 = probabilities.mapIndexed { index, value -> index to value }
                .sortedByDescending { it.second }
                .take(3)

            top3.forEachIndexed { rank, (i, conf) ->
                Log.d("Classifier", "Top-${rank + 1}: ${labels[i]} (Confidence: $conf)")
            }

            val predictedLabel = labels[maxIndex]

            return if (predictedLabel == "Unknown" || confidence < CONFIDENCE_THRESHOLD) {
                Log.d("Classifier", "Prediksi tidak yakin atau 'Unknown'. Confidence: $confidence")
            Pair("Tanaman Tidak Diketahui", confidence)
        } else {
            Log.d("Classifier", "Prediksi: $predictedLabel, Confidence: $confidence")
            Pair(predictedLabel, confidence)
        }

        } catch (e: Exception) {
            Log.e("Classifier", "Terjadi kesalahan saat inferensi: ${e.message}")
            Pair("Tanaman Tidak Diketahui", 0.0f)
        }
    }

    private fun preprocessImage(bitmap: Bitmap): TensorImage {
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(inputImageSize, inputImageSize, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()

        return imageProcessor.process(tensorImage)
    }

    fun close() {
        interpreter.close()
    }
}
