package psilox.graphics

import glm.vec2.Vec2
import org.lwjgl.opengl.GL11.*
import psilox.resource.ResourceLibrary
import psilox.resource.TextureResource
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class Texture {

    val width: Int
    val height: Int
    val dimensions: Vec2
    lateinit internal var texture: TextureResource

    constructor(path: String) {
        val image = ImageIO.read(Texture::class.java.getResourceAsStream(path))
        width = image.width
        height = image.height
        dimensions = Vec2(image.width, image.height)
        create(convertImage(image))
        ResourceLibrary.addReference(texture)
    }

    constructor(width: Int, height: Int) {
        this.width = width
        this.height = height
        this.dimensions = Vec2(width, height)
        create()
        ResourceLibrary.addReference(texture)
    }

    constructor(dim: Vec2): this(dim.x.toInt(), dim.y.toInt())

    protected fun finalize() {
        ResourceLibrary.remReference(texture)
    }

    private fun create(data: IntArray? = null) {
        texture = TextureResource(glGenTextures())
        bind()
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        if(data == null) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0)
        } else {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data)
        }
        unbind()
    }

    private fun convertImage(image: BufferedImage): IntArray {
        val result = IntArray(image.width * image.height)
        image.getRGB(0, 0, width, height, result, 0, width)
        val ac = 0xFF000000.toInt()
        val rc = 0xFF0000
        val gc = 0xFF00
        val bc = 0xFF
        result.forEachIndexed { index, i ->
            result[index] = (i and ac) or ((i and bc) shl 16) or (i and gc) or ((i and rc) shr 16)
        }
        return result
    }

    fun bind() {
        glBindTexture(GL_TEXTURE_2D, texture.glReference)
    }

    fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0)
    }

}