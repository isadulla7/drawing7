package dot.isadulla.dotpaint.tools.line

import android.view.MotionEvent
import dot.isadulla.dotpaint.api.shape.ShapeBuilder

class LineBuilder : dot.isadulla.dotpaint.api.shape.ShapeBuilder {
    private var startX = 0f
    private var startY = 0f
    private var endX = 0f
    private var endY = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                endX = event.x
                endY = event.y
            }
        }
        return true
    }

    override fun build(): LineShape {
        return LineShape(startX, startY, endX, endY)
    }

    override fun reset() {
        startX = 0f; startY = 0f; endX = 0f; endY = 0f
    }
}