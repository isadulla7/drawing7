package dot.isaulla.tools

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.MotionEvent

enum class ShapeType {
    LINE, RECTANGLE, CIRCLE
}

class ShapeTool(
    private val shapeType: ShapeType,
    private val paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = 0xFF000000.toInt() // Qora rang
    }
) : Tool {
    private var startX = 0f
    private var startY = 0f
    private var endX = 0f
    private var endY = 0f
    private val path = Path()

    override fun onTouchEvent(event: MotionEvent, canvas: Canvas) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                path.reset()
            }
            MotionEvent.ACTION_MOVE -> {
                endX = event.x
                endY = event.y
                drawShape(canvas)
            }
            MotionEvent.ACTION_UP -> {
                endX = event.x
                endY = event.y
                drawShape(canvas)
            }
        }
    }

    private fun drawShape(canvas: Canvas) {
        path.reset()
        when (shapeType) {
            ShapeType.LINE -> {
                path.moveTo(startX, startY)
                path.lineTo(endX, endY)
                canvas.drawPath(path, paint)
            }
            ShapeType.RECTANGLE -> {
                val rect = RectF(
                    minOf(startX, endX),
                    minOf(startY, endY),
                    maxOf(startX, endX),
                    maxOf(startY, endY)
                )
                canvas.drawRect(rect, paint)
            }
            ShapeType.CIRCLE -> {
                val radius = kotlin.math.hypot((endX - startX).toDouble(), (endY - startY).toDouble()).toFloat() / 2
                val centerX = (startX + endX) / 2
                val centerY = (startY + endY) / 2
                canvas.drawCircle(centerX, centerY, radius, paint)
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