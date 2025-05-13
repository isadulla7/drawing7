package dot.isadulla.dotpaint.tools.line

import android.graphics.Canvas
import android.graphics.Paint
import dot.isadulla.dotpaint.api.shape.Shape
import dot.isadulla.dotpaint.api.shape.ShapePainter

class LinePainter(private val paint: Paint) : ShapePainter {
    override fun drawShape(canvas: Canvas?, shape: Shape) {
        if (shape is LineShape) {
            canvas?.drawLine(shape.startX, shape.startY, shape.endX, shape.endY, paint)
        }
    }
}