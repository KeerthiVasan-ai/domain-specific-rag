package com.example.ondevice_rag.rag

import com.example.ondevice_rag.tflite.RagTFLiteModel

class RagRepository {

    private val chunks = mutableListOf<RagChunk>()

    fun add(chunk: RagChunk) {
        chunks.add(chunk)
    }

    fun getAll(): List<RagChunk> = chunks

    fun findByText(text: String): RagChunk =
        chunks.first { it.text == text }

    fun reEmbedAll(model: RagTFLiteModel) {
        chunks.forEachIndexed { index, chunk ->
            chunks[index] = chunk.copy(
                embedding = model.embed(chunk.text)
            )
        }
    }
}
