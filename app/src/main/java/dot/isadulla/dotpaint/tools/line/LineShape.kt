package dot.isadulla.dotpaint.tools.line

import android.graphics.RectF
import dot.isadulla.dotpaint.api.shape.Shape
import dot.isadulla.dotpaint.api.shape.ShapePainter
import java.util.UUID

class LineShape(
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float
) : dot.isadulla.dotpaint.api.shape.Shape {

    override val id: String = UUID.randomUUID().toString()

    override fun draw(painter: dot.isadulla.dotpaint.api.shape.ShapePainter) {
        painter.drawShape(canvas = null, shape = this) // canvas is passed from engine
    }

    override fun contains(x: Float, y: Float): Boolean {
        // Simple hit test: bounding box check
        val bounds = getBounds()
        return bounds.contains(x, y)
    }

    override fun getBounds(): RectF = RectF(
        minOf(startX, endX),
        minOf(startY, endY),
        maxOf(startX, endX),
        maxOf(startY, endY)
    )
}
