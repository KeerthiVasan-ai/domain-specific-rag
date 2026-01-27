package com.example.ondevice_rag

import com.example.ondevice_rag.rag.*
import com.example.ondevice_rag.tflite.RagTFLiteModel
import com.example.ondevice_rag.util.Document
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    private val channel = "agentic_rag/channel"
    private lateinit var ragEngine: RagEngine

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        val model = RagTFLiteModel(this)
        val repository = RagRepository()

        val chunks = chunkDocument(Document.trainedContent)

        chunks.forEachIndexed { index, text ->
            repository.add(
                RagChunk(
                    id = index,
                    text = text,
                    embedding = model.embed(text)
                )
            )
        }

        ragEngine = RagEngine(model, repository)

        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            channel
        ).setMethodCallHandler { call, result ->

            when (call.method) {

                "ask" -> {
                    val question = call.argument<String>("question")!!

                    val results = ragEngine.retrieveTopK(question)

                    result.success(
                        mapOf(
                            "answer" to results.first().chunk.text,
                            "suggestions" to results.drop(1).map { it.chunk.text }
                        )
                    )
                }

                "train" -> {
                    val question = call.argument<String>("question")!!
                    val positive = call.argument<String>("positive")!!
                    val negative = call.argument<String>("negative")!!

                    val posChunk = repository.findByText(positive)
                    val negChunk = repository.findByText(negative)

                    ragEngine.trainWithFeedback(question, posChunk, negChunk)

                    // Return success only - don't re-query. Next "ask" will use updated embeddings.
                    result.success(
                        mapOf("success" to true)
                    )
                }

                else -> result.notImplemented()
            }
        }
    }
    fun chunkDocument(text: String): List<String> {
        return text
            .split(Regex("(?<=[.!?])\\s+"))
            .map { it.trim() }
            .filter { it.split(" ").size >= 6 }
    }

}
