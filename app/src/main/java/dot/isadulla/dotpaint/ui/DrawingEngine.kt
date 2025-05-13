package dot.isadulla.dotpaint.ui

import android.graphics.Canvas
import dot.isadulla.dotpaint.api.shape.ShapePainter
import dot.isadulla.dotpaint.core.model.DrawingState

class DrawingEngine(
    private val state: DrawingState,
    private val painter: ShapePainter
) {
    fun render(canvas: Canvas) {
        for (shape in state.shapes) {
            shape.draw(painter)
        }
    }
}