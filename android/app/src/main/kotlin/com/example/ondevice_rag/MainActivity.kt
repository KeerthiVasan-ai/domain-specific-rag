package com.example.ondevice_rag

import android.util.Log
import com.example.ondevice_rag.db.ObjectBox
import com.example.ondevice_rag.rag.*
import com.example.ondevice_rag.tflite.RagTFLiteModel
import com.example.ondevice_rag.util.Document
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    private val channel = "agentic_rag/channel"
    private lateinit var ragEngine: RagEngine
    private lateinit var repository: RagRepository

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // Initialize ObjectBox database
        ObjectBox.init(this)

        val model = RagTFLiteModel(this)
        repository = RagRepository()

        // Only add chunks if database is empty (first run)
        if (repository.count() == 0L) {
            val chunks = chunkDocument(Document.newDocument)
            Log.d("RAG_DEBUG", "Chunked document into ${chunks.size} chunks")
            chunks.forEachIndexed { index, text ->
                repository.add(RagChunk(id = 0, text = text, embedding = model.embed(text)))
            }
            Log.d("RAG_DEBUG", "Inserted chunks. Total count: ${repository.count()}")
        } else {
            Log.d("RAG_DEBUG", "Database already has ${repository.count()} chunks")
        }

        ragEngine = RagEngine(model, repository)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channel).setMethodCallHandler {
                call,
                result ->
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

                    if (posChunk != null && negChunk != null) {
                        ragEngine.trainWithFeedback(question, posChunk, negChunk)
                        result.success(mapOf("success" to true))
                    } else {
                        result.error("CHUNK_NOT_FOUND", "Could not find chunks", null)
                    }
                }
                else -> result.notImplemented()
            }
        }
    }

    fun chunkDocument(text: String): List<String> {
        return text.split(Regex("(?<=[.!?])\\s+")).map { it.trim() }.filter {
            it.split(" ").size >= 6
        }
    }
}
