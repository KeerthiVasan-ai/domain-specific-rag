package com.example.ondevice_rag.rag

import com.example.ondevice_rag.tflite.RagTFLiteModel

class RagEngine(
    private val model: RagTFLiteModel,
    private val repository: RagRepository
) {

    /** Retrieval = Python retrieve_top_k */
    fun retrieveTopK(question: String, k: Int = 3): List<RagResult> {
        val queryEmb = model.embed(question)

        return repository.getAll()
            .map { chunk ->
                RagResult(
                    chunk = chunk,
                    score = model.score(queryEmb, chunk.embedding)
                )
            }
            .sortedByDescending { it.score }
            .take(k)
    }

    /** Training = Python trainer.train + re-embed */
    fun trainWithFeedback(
        question: String,
        positiveChunk: RagChunk,
        negativeChunk: RagChunk
    ) {
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
