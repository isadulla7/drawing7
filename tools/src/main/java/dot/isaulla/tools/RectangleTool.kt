package dot.isaulla.tools

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import kotlin.math.max
import kotlin.math.min

class RectangleTool(
    private val paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = 0xFF000000.toInt()
    }
) : Tool {
    private var startX = 0f
    private var startY = 0f
    private var rect = RectF()

    override fun onTouchEvent(event: MotionEvent, canvas: Canvas) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                rect.set(
                    min(startX, event.x),
                    min(startY, event.y),
                    max(startX, event.x),
                    max(startY, event.y)
                )
                canvas.drawRect(rect, paint)
            }

            MotionEvent.ACTION_UP -> {
                rect.set(
                    min(startX, event.x),
                    min(startY, event.y),
                    max(startX, event.x),
                    max(startY, event.y)
                )
                canvas.drawRect(rect, paint)
            }
        }
    }

    fun setColor(color: Int) {
        paint.color = color
    }

    fun setStrokeWidth(width: Float) {
        paint.strokeWidth = width
    }
}