package psilox.math

val degToRad: Float = ((3.1415926535 / 180).toFloat())

val Number.radians: Float
    get() = this.toFloat() * degToRad