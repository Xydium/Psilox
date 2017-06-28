package psilox.graphics

import glm.vec2.Vec2
import glm.vec3.Vec3
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
import psilox.resource.ResourceLibrary
import psilox.resource.VAOResource
import psilox.resource.VBOResource
import psilox.utility.createFloatBuffer
import psilox.utility.toFloatArray

class Mesh private constructor(positions: Array<Vec3>, tcoords: Array<Vec2>) {

    internal val vao: VAOResource
    internal val vboPos: VBOResource
    internal val vboTex: VBOResource
    internal val verts: Int

    init {
        vao = createVAO()
        vboPos = storeDataInAttribList(VERTEX, positions.toFloatArray(), 3)
        vboTex = storeDataInAttribList(TCOORD, tcoords.toFloatArray(), 2)
        verts = positions.size
        unbindVAO()

        with(ResourceLibrary) {
            addReference(vao)
            addReference(vboPos)
            addReference(vboTex)
        }
    }

    protected fun finalize() {
        with(ResourceLibrary) {
            remReference(vao)
            remReference(vboPos)
            remReference(vboTex)
        }
    }

    companion object {

        val VERTEX = 0
        val TCOORD = 1

        private fun createVAO(): VAOResource {
            val vao = VAOResource(glGenVertexArrays())
            glBindVertexArray(vao.glReference)
            return vao
        }

        private fun storeDataInAttribList(attrib: Int, data: FloatArray, size: Int): VBOResource {
            val vbo = VBOResource(glGenBuffers())
            glBindBuffer(GL_ARRAY_BUFFER, vbo.glReference)
            val buffer = createFloatBuffer(data)
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
            glVertexAttribPointer(attrib, size, GL_FLOAT, false, 0, 0)
            glBindBuffer(GL_ARRAY_BUFFER, 0)
            return vbo
        }

        private fun unbindVAO() = glBindVertexArray(0)

        fun unitSquare(): Mesh {
            val verts = arrayOf(
                    Vec3(0, 1, 0),
                    Vec3(0, 0, 0),
                    Vec3(1, 0, 0),
                    Vec3(1, 0, 0),
                    Vec3(1, 1, 0),
                    Vec3(0, 1, 0)
            )

            val tcoords = arrayOf(
                    Vec2(0, 0),
                    Vec2(0, 1),
                    Vec2(1, 1),
                    Vec2(1, 1),
                    Vec2(1, 0),
                    Vec2(0, 0)
            )

            return Mesh(verts, tcoords)
        }

    }

}