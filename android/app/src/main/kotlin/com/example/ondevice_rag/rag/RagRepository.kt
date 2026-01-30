package com.example.ondevice_rag.rag

import android.util.Log
import com.example.ondevice_rag.db.ObjectBox
import com.example.ondevice_rag.db.RagChunkEntity
import com.example.ondevice_rag.db.RagChunkEntity_
import com.example.ondevice_rag.tflite.RagTFLiteModel
import io.objectbox.Box

class RagRepository {

    private val box: Box<RagChunkEntity> by lazy {
        ObjectBox.boxStore.boxFor(RagChunkEntity::class.java)
    }

    fun add(chunk: RagChunk) {
        box.put(RagChunkEntity.from(chunk))
    }

    fun addAll(chunks: List<RagChunk>) {
        box.put(chunks.map { RagChunkEntity.from(it) })
    }

    fun getAll(): List<RagChunk> = box.all.map { it.toRagChunk() }

    fun findByText(text: String): RagChunk? =
            box.query(RagChunkEntity_.text.equal(text)).build().findFirst()?.toRagChunk()

    /**
     * ObjectBox HNSW vector similarity search. Returns top-k nearest neighbors with their
     * distances. Lower distance = more similar.
     */
    fun findNearestNeighbors(queryEmbedding: FloatArray, k: Int): List<Pair<RagChunk, Float>> {
        Log.d(
                "RAG_DEBUG",
                "findNearestNeighbors called with k=$k, total chunks in DB: ${box.count()}"
        )

        val query = box.query(RagChunkEntity_.embedding.nearestNeighbors(queryEmbedding, k)).build()
        val results = query.findWithScores()

        Log.d("RAG_DEBUG", "HNSW query returned ${results.size} results")
        results.forEachIndexed { index, result ->
            Log.d(
                    "RAG_DEBUG",
                    "Result $index: score=${result.score}, text=${result.get().text.take(50)}..."
            )
        }

        return results.map { result -> result.get().toRagChunk() to result.score.toFloat() }
    }

    fun reEmbedAll(model: RagTFLiteModel) {
        val all = box.all
        all.forEach { entity -> entity.embedding = model.embed(entity.text) }
        box.put(all)
    }

    fun clear() {
        box.removeAll()
    }

    fun count(): Long = box.count()
}
