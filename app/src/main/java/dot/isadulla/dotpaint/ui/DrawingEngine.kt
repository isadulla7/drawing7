package dot.isadulla.dotpaint.ui

import android.graphics.Canvas
import android.view.MotionEvent
import dot.isadulla.dotpaint.api.shape.Shape
import dot.isadulla.dotpaint.api.shape.ShapeBuilder
import dot.isadulla.dotpaint.api.shape.ShapePainter
import dot.isadulla.dotpaint.core.model.DrawingState
import dot.isadulla.dotpaint.di.ServiceCenter

class DrawingEngine(
    private val state: DrawingState,
    private val painter: ShapePainter
) {
    private val builder: ShapeBuilder = ServiceCenter.get()

    private var currentShape: Shape? = null

    fun onTouchEvent(event: MotionEvent) {
        currentShape = builder.build(event)
        if (event.action == MotionEvent.ACTION_UP) {
            currentShape?.let { state.shapes.add(it) }
        }
    }

    fun render(canvas: Canvas) {
        for (shape in state.shapes) {
            painter.drawShape(canvas, shape)
        }
        currentShape?.let {
            painter.drawShape(canvas, it)
        }
    }
}