package dot.isadulla.dotpaint.api.shape

import android.graphics.RectF

interface Shape {
    val id: String
    fun draw(painter: ShapePainter)
    fun contains(x: Float, y: Float): Boolean
    fun getBounds(): RectF
}
