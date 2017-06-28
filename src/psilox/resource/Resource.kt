package psilox.resource

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*

abstract class Resource(val glReference: Int) {

    internal var references = 0
    internal var registered = false

    open fun destroy() { registered = false; }

}

class VAOResource(vaoID: Int): Resource(vaoID) {

    override fun destroy() {
        super.destroy()
        glDeleteVertexArrays(glReference)
    }

}

class VBOResource(vboID: Int): Resource(vboID) {

    override fun destroy() {
        super.destroy()
        glDeleteBuffers(glReference)
    }

}

class ShaderResource(programID: Int): Resource(programID) {

    override fun destroy() {
        super.destroy()
        glDeleteProgram(glReference)
    }

}