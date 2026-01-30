package com.example.ondevice_rag.rag

import com.example.ondevice_rag.tflite.RagTFLiteModel

class RagEngine(private val model: RagTFLiteModel, private val repository: RagRepository) {

    /** Retrieval using ObjectBox HNSW vector search */
    fun retrieveTopK(question: String, k: Int = 3): List<RagResult> {
        val queryEmb = model.embed(question)

        // Use ObjectBox HNSW index for O(log n) similarity search
        return repository.findNearestNeighbors(queryEmb, k).map { (chunk, distance) ->
            RagResult(
                    chunk = chunk,
                    // Convert distance to similarity score (lower distance = higher similarity)
                    score = 1f / (1f + distance)
            )
        }
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
