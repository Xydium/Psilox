package psilox.graphics

val WHITE = Color(1F, 1F, 1F, 1F);
val BLACK = Color(0F, 0F, 0F, 1F);
val CLEAR = Color(0F, 0F, 0F, 0F);
val RED = Color(1F, 0F, 0F, 1F);
val BLUE = Color(0F, 0F, 1F, 1F);
val GREEN = Color(0F, 1F, 0F, 1F);
val GRAY = Color(.5F, .5F, .5F, 1F);

class Color(val r: Float,
            val g: Float,
            val b: Float,
            val a: Float) {

    fun asArray() = arrayOf(r, g, b, a);

}