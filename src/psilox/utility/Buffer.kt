package psilox.utility

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

fun createByteBuffer(data: Array<Byte>): ByteBuffer {
    val result = ByteBuffer.allocateDirect(data.size).order(ByteOrder.nativeOrder())
    result.put(data.toByteArray()).flip()
    return result
}

fun createFloatBuffer(data: Array<Float>): FloatBuffer {
    val result = ByteBuffer.allocateDirect(data.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
    result.put(data.toFloatArray()).flip()
    return result
}

fun createIntBuffer(data: Array<Int>): IntBuffer {
    val result = ByteBuffer.allocateDirect(data.size * 4).order(ByteOrder.nativeOrder()).asIntBuffer()
    result.put(data.toIntArray()).flip()
    return result
}