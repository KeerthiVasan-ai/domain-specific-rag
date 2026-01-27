package com.example.ondevice_rag.tflite


import android.content.Context
import com.example.ondevice_rag.util.AssetUtils
import org.tensorflow.lite.Interpreter
import java.nio.FloatBuffer


class RagTFLiteModel(context: Context) {

    private val interpreter: Interpreter
    private val embedDim = 128

    init {
        val buffer = AssetUtils.loadModel(context, "rag_model_new_test.tflite")
        val options = Interpreter.Options().apply {
            setUseXNNPACK(false)
        }

        interpreter = Interpreter(buffer, options)
    }

    fun embed(text: String): FloatArray {
        val output = FloatArray(embedDim)

        interpreter.runSignature(
            mapOf("text" to text),
            mapOf("output_0" to output),
            "infer"
        )
        return output
    }

    fun train(query: String, positive: String, negative: String) {
        interpreter.runSignature(
            mapOf(
                "q" to query,
                "pos" to positive,
                "neg" to negative
            ),
            emptyMap(),
            "train"
        )
    }

    fun score(queryEmb: FloatArray, docEmb: FloatArray): Float {
        val outputBuffer = FloatBuffer.allocate(1)

        interpreter.runSignature(
            mapOf(
                "q_emb" to queryEmb,
                "d_emb" to docEmb
            ),
            mapOf(
                "output_0" to outputBuffer
            ),
            "score"
        )

        return outputBuffer.get(0)
    }
}
