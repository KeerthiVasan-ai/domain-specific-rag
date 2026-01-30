package com.example.ondevice_rag.db

import com.example.ondevice_rag.rag.RagChunk
import io.objectbox.annotation.Entity
import io.objectbox.annotation.HnswIndex
import io.objectbox.annotation.Id
import io.objectbox.annotation.VectorDistanceType

@Entity
data class RagChunkEntity(
    @Id var id: Long = 0,
    var text: String = "",
    @HnswIndex(dimensions = 128, distanceType = VectorDistanceType.COSINE)
    var embedding: FloatArray = FloatArray(0)
) {
    companion object {
        fun from(chunk: RagChunk): RagChunkEntity =
            RagChunkEntity(
                id = chunk.id.toLong(),
                text = chunk.text,
                embedding = chunk.embedding
            )
    }

    fun toRagChunk(): RagChunk = RagChunk(id = id.toInt(), text = text, embedding = embedding)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RagChunkEntity

        if (id != other.id) return false
        if (text != other.text) return false
        if (!embedding.contentEquals(other.embedding)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + embedding.contentHashCode()
        return result
    }
}
