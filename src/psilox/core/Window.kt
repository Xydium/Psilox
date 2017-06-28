package psilox.core

import glm.glm
import glm.mat4x4.Mat4
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_MULTISAMPLE
import org.lwjgl.opengl.GL20.glDisableVertexAttribArray
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil.NULL
import psilox.graphics.*
import java.util.*
import glm.vec3.Vec3
import psilox.math.radians
import psilox.math.randOf

class Window(val title: String = "Psilox",
             val width: Int = 500,
             val height: Int = 500,
             val fullscreen: Boolean = false,
             val undecorated: Boolean = false,
             val antiAlias: Boolean = false,
             val clear: Color = WHITE,
             val zDepth: Float = 100F
            ) {

    var projection = glm.ortho(0F, width.toFloat(), 0F, height.toFloat(), -zDepth / 2, zDepth / 2)
    var transforms = Stack<Mat4>()
    var handle = 0L

    lateinit var defaultMesh: Mesh
    lateinit var defaultShader: Shader
    lateinit var debugTexture: Texture

    internal fun initialize() {
        if(!glfwInit()) {
            error("Initializing GLFW failed.")
            return
        }

        glfwWindowHint(GLFW_RESIZABLE, 0)

        glfwWindowHint(GLFW_DECORATED, if (this.undecorated) 0 else 1)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        if (antiAlias) glfwWindowHint(GLFW_SAMPLES, 8)

        handle = glfwCreateWindow(width, height, title, NULL, NULL)

        if (handle == NULL) {
            error("Creating GLFW window failed.")
            return
        }

        val m = glfwGetVideoMode(glfwGetPrimaryMonitor())
        glfwSetWindowPos(handle, (m.width() - width) / 2, (m.height() - height) / 2)

        if (fullscreen) {
            glfwSetWindowMonitor(handle, glfwGetPrimaryMonitor(), 0, 0, m.width(), m.height(), 60)
        }

        glfwMakeContextCurrent(handle);
        glfwShowWindow(handle);
        createCapabilities();

        if(antiAlias) glEnable(GL_MULTISAMPLE)
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        glClearColor(clear.r, clear.g, clear.b, clear.a)

        if(fullscreen) {
            glViewport(0, 0, m.width(), m.height());
        } else {
            glViewport(0, 0, width, height);
        }

        println("Created Window using GL: ${getGLVersion()}")

        defaultMesh = Mesh.unitSquare()
        defaultShader = Shader("/psilox.glsl")
        debugTexture = Texture("/0e9.jpg")
    }

    internal fun pollEvents() = glfwPollEvents()
    internal fun getGLVersion() = glGetString(GL_VERSION)
    internal fun swapBuffers() = glfwSwapBuffers(handle)
    internal fun clear() = glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    internal fun shouldClose() = glfwWindowShouldClose(handle)

    internal fun logLastError() {
        val e = glGetError()
        if (e != 0) {
            error("GL Error $e")
        }
    }

    internal fun terminate() {
        glfwDestroyWindow(handle)
        glfwTerminate()
    }

    internal fun debugRender() {
        glBindVertexArray(defaultMesh.vao.glReference)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)

        defaultShader.enable()
        debugTexture.bind()
        defaultShader["u_projection"] = projection
        defaultShader["u_transform"] = Mat4().translate(Vec3(250, 250, 0)).rotate(Psilox.ticks.radians, Vec3(0, 0, 1)).scale(200F)
        defaultShader["u_modulate"] = randOf(RED, GREEN, WHITE, BLUE)
        glDrawArrays(GL_TRIANGLES, 0, defaultMesh.verts)
        debugTexture.unbind()
        defaultShader.disable()

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glBindVertexArray(0)
    }

}