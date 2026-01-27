package com.example.ondevice_rag.util

import android.content.Context
import java.nio.ByteBuffer
import java.nio.ByteOrder

object AssetUtils {

    fun loadModel(context: Context, fileName: String): ByteBuffer {
        val bytes = context.assets.open(fileName).readBytes()
        return ByteBuffer.allocateDirect(bytes.size)
            .order(ByteOrder.nativeOrder())
            .put(bytes)
            .apply { rewind() }
    }
}
