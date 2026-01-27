package com.example.ondevice_rag.rag

data class RagResult(
    val chunk: RagChunk,
    val score: Float
)
