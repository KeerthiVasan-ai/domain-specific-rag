package com.example.ondevice_rag.rag

import com.example.ondevice_rag.tflite.RagTFLiteModel

class RagEngine(private val model: RagTFLiteModel, private val repository: RagRepository) {

    /** Retrieval using ObjectBox HNSW vector search with TFLite re-scoring */
    fun retrieveTopK(question: String, k: Int = 4): List<RagResult> {
        val queryEmb = model.embed(question)

        // Use ObjectBox HNSW index for O(log n) similarity search
        // Then re-rank using TFLite's score function for accurate cosine similarity (matching
        // Python)
        return repository
                .findNearestNeighbors(queryEmb, k)
                .map { (chunk, _) ->
                    // Use TFLite model's score function (dot product of normalized vectors = cosine
                    // similarity)
                    val score = model.score(queryEmb, chunk.embedding)
                    RagResult(chunk = chunk, score = score)
                }
                .sortedByDescending { it.score }
    }

    /** Training = Python trainer.train + re-embed */
    fun trainWithFeedback(question: String, positiveChunk: RagChunk, negativeChunk: RagChunk) {
        // 🔹 Small number of steps = stable learning (matches Python better)
        repeat(5) {
            model.train(
                    query = question,
                    positive = positiveChunk.text,
                    negative = negativeChunk.text
            )
        }

        // 🔹 Critical: re-embed all chunks AFTER training
        repository.reEmbedAll(model)
    }
}
