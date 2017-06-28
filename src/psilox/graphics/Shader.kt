package psilox.graphics

import glm.mat4x4.Mat4
import glm.vec2.Vec2
import glm.vec3.Vec3
import glm.vec4.Vec4
import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL20.*
import psilox.graphics.Mesh.Companion.TCOORD
import psilox.graphics.Mesh.Companion.VERTEX
import psilox.math.Region
import psilox.resource.ResourceLibrary
import psilox.resource.ShaderResource
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Shader(path: String) {

    val program: ShaderResource
    private var enabled = false
    private val uniforms = HashMap<String, Int>()

    init {
        program = load(path)
        ResourceLibrary.addReference(program)
        bindAttribute(VERTEX, "v_position")
        bindAttribute(TCOORD, "v_tcoord")
    }

    protected fun finalize() {
        ResourceLibrary.remReference(program)
    }

    fun enable() {
        glUseProgram(program.glReference)
        enabled = true
    }

    fun disable() {
        glUseProgram(0)
        enabled = false
    }

    operator fun set(index: String, value: Any) {
        val loc: Int = if(uniforms.containsKey(index)) uniforms.get(index)!! else {
            val result = glGetUniformLocation(program.glReference, index)
            if(result != -1) {
                uniforms.put(index, result)
                result
            } else {
                throw IllegalArgumentException("No such uniform with name $index in this shader program!")
            }
        }

        when(value) {
            is Int -> glUniform1i(loc, value)
            is Float -> glUniform1f(loc, value)
            is Vec2 -> glUniform2f(loc, value.x, value.y)
            is Vec3 -> glUniform3f(loc, value.x, value.y, value.z)
            is Vec4 -> glUniform4f(loc, value.x, value.y, value.z, value.w)
            is Color -> glUniform4f(loc, value.r, value.g, value.b, value.a)
            is Region -> glUniform4f(loc, value.bottomLeft.x, value.bottomLeft.y, value.topRight.x, value.topRight.y)
            is Mat4 -> glUniformMatrix4fv(loc, false, value.to(ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asFloatBuffer()))
        }
    }

    private fun bindAttribute(attrib: Int, name: String) {
        glBindAttribLocation(program.glReference, attrib, name)
    }

    private fun load(path: String): ShaderResource {
        val text = Shader::class.java.getResource(path).readText()
        val fragStart = text.indexOf("#version 400 core", startIndex = 1)
        val vert = text.substring(0..fragStart)
        val frag = text.substring(fragStart)
        return create(vert, frag)
    }

    private fun create(vert: String, frag: String): ShaderResource {
        val program = ShaderResource(glCreateProgram())

        val vertID = glCreateShader(GL_VERTEX_SHADER)
        val fragID = glCreateShader(GL_FRAGMENT_SHADER)
        glShaderSource(vertID, vert)
        glShaderSource(fragID, frag)

        glCompileShader(vertID)
        if (glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
            throw Exception("Failed to compile vertex shader!" + glGetShaderInfoLog(vertID))
        }

        glCompileShader(fragID);
        if (glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
            throw Exception("Failed to compile fragment shader!" + glGetShaderInfoLog(fragID))
        }

        glAttachShader(program.glReference, vertID)
        glAttachShader(program.glReference, fragID)
        glLinkProgram(program.glReference)
        if(glGetProgrami(program.glReference, GL_LINK_STATUS) == GL_FALSE) {
            throw Exception("Failed to link shader program!" + glGetProgramInfoLog(program.glReference))
        }

        glValidateProgram(program.glReference)

        glDetachShader(program.glReference, vertID)
        glDetachShader(program.glReference, fragID)

        glDeleteShader(vertID)
        glDeleteShader(fragID)

        return program
    }

}