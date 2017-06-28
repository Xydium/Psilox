package psilox.math

private val mr
    get() = Math.random()

infix fun Int.randTo(high: Int) = (mr * (high - this) + this).toInt()
infix fun Float.randTo(high: Float) = (mr * (high - this) + this).toFloat()
infix fun Double.randTo(high: Double) = mr * (high - this) + this

fun <E> randOf(vararg choices: E): E {
    return choices[0 randTo choices.size]
}