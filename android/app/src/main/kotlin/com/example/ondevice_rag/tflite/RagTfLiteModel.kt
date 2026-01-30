package com.example.ondevice_rag.tflite

import android.content.Context
import com.example.ondevice_rag.util.AssetUtils
import java.nio.FloatBuffer
import org.tensorflow.lite.Interpreter

class RagTFLiteModel(context: Context) {

    private val interpreter: Interpreter
    private val embedDim = 128

    init {
        val buffer = AssetUtils.loadModel(context, "rag_model_new_test.tflite")
        val options = Interpreter.Options().apply { setUseXNNPACK(false) }

        interpreter = Interpreter(buffer, options)
    }

    fun embed(text: String): FloatArray {
        val output = FloatArray(embedDim)

        interpreter.runSignature(mapOf("text" to text), mapOf("output_0" to output), "infer")
        return l2Normalize(output)
    }

    /**
     * L2 normalize the embedding vector to match Python's tf.math.l2_normalize behavior. This is
     * critical for cosine similarity to work correctly.
     */
    private fun l2Normalize(vector: FloatArray): FloatArray {
        var norm = 0f
        for (v in vector) {
            norm += v * v
        }
        norm = kotlin.math.sqrt(norm)

        // Avoid division by zero
        if (norm == 0f) return vector

        return FloatArray(vector.size) { i -> vector[i] / norm }
    }

    fun train(query: String, positive: String, negative: String) {
        interpreter.runSignature(
                mapOf("q" to query, "pos" to positive, "neg" to negative),
                emptyMap(),
                "train"
        )
    }

    fun score(queryEmb: FloatArray, docEmb: FloatArray): Float {
        val outputBuffer = FloatBuffer.allocate(1)

        interpreter.runSignature(
                mapOf("q_emb" to queryEmb, "d_emb" to docEmb),
                mapOf("output_0" to outputBuffer),
                "score"
        )

        return outputBuffer.get(0)
    }
}
