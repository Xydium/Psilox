package psilox.utility

import glm.vec1.Vec1
import glm.vec2.Vec2
import glm.vec3.Vec3
import glm.vec4.Vec4
import psilox.graphics.Color
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

fun createByteBuffer(data: ByteArray): ByteBuffer {
    val result = ByteBuffer.allocateDirect(data.size).order(ByteOrder.nativeOrder())
    result.put(data).flip()
    return result
}

fun createFloatBuffer(data: FloatArray): FloatBuffer {
    val result = ByteBuffer.allocateDirect(data.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
    result.put(data).flip()
    return result
}

fun createIntBuffer(data: IntArray): IntBuffer {
    val result = ByteBuffer.allocateDirect(data.size * 4).order(ByteOrder.nativeOrder()).asIntBuffer()
    result.put(data).flip()
    return result
}

internal fun Array<Vec1>.toFloatArray(): FloatArray {
    val result = FloatArray(this.size)
    forEachIndexed { index, vec ->
        result[index] = vec.x
    }
    return result
}

internal fun Array<Vec2>.toFloatArray(): FloatArray {
    val result = FloatArray(this.size * 2)
    forEachIndexed { index, vec ->
        result[index * 2 + 0] = vec.x
        result[index * 2 + 1] = vec.y
    }
    return result
}

internal fun Array<Vec3>.toFloatArray(): FloatArray {
    val result = FloatArray(this.size * 3)
    forEachIndexed { index, vec ->
        result[index * 3 + 0] = vec.x
        result[index * 3 + 1] = vec.y
        result[index * 3 + 2] = vec.z
    }
    return result
}

internal fun Array<Vec4>.toFloatArray(): FloatArray {
    val result = FloatArray(this.size * 4)
    forEachIndexed { index, vec ->
        result[index * 4 + 0] = vec.x
        result[index * 4 + 1] = vec.y
        result[index * 4 + 2] = vec.z
        result[index * 4 + 3] = vec.w
    }
    return result
}

internal fun Array<Color>.toFloatArray(): FloatArray {
    val result = FloatArray(this.size * 4)
    forEachIndexed { index, color ->
        result[index * 4 + 0] = color.r
        result[index * 4 + 1] = color.g
        result[index * 4 + 2] = color.b
        result[index * 4 + 3] = color.a
    }
    return result
}