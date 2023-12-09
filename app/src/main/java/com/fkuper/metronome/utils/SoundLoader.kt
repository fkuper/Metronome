package com.fkuper.metronome.utils

import com.google.common.primitives.Bytes
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset

/**
 * Source: https://github.com/Kr0oked/Metronome/blob/master/app/src/main/java/com/bobek/metronome/audio/SoundLoader.kt
 */
object SoundLoader {

    private const val DATA_CHUNK_SIZE = 8

    private val DATA_MARKER = "data".toByteArray(Charset.forName("ASCII"))

    @JvmStatic
    fun readDataFromWavPcmFloat(input: InputStream): FloatArray {
        val content = input.readBytes()
        val indexOfDataMarker = Bytes.indexOf(content, DATA_MARKER)
        check(indexOfDataMarker >= 0) { "Could not find data marker in the content" }

        val startOfSound = indexOfDataMarker + DATA_CHUNK_SIZE
        check(startOfSound <= content.size) { "Too short data chunk" }

        val byteBuffer = ByteBuffer.wrap(content, startOfSound, content.size - startOfSound)
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
        val floatBuffer = byteBuffer.asFloatBuffer()

        val floatArray = FloatArray(floatBuffer.remaining())
        floatBuffer[floatArray]
        return floatArray
    }
}