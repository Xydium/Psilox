package psilox.math

import glm.vec2.Vec2

class Region(val bottomLeft: Vec2, val topRight: Vec2) {

    constructor(xbl: Float, ybl: Float, xtr: Float, ytr: Float): this(Vec2(xbl, ybl), Vec2(xtr, ytr))

}