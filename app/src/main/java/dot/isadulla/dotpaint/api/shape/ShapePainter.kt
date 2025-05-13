package dot.isadulla.dotpaint.api.shape

import android.graphics.Canvas

interface ShapePainter {
    fun drawShape(canvas: Canvas?, shape: Shape)
}