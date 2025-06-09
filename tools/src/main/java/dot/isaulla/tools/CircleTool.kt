package dot.isaulla.tools

import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import kotlin.math.hypot

class CircleTool(
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

    override fun onTouchEvent(event: MotionEvent, canvas: Canvas) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                endX = event.x
                endY = event.y
                drawCircle(canvas)
            }
            MotionEvent.ACTION_UP -> {
                endX = event.x
                endY = event.y
                drawCircle(canvas)
            }
        }
    }

    private fun drawCircle(canvas: Canvas) {
        val radius = hypot((endX - startX).toDouble(), (endY - startY).toDouble()).toFloat() / 2
        val centerX = (startX + endX) / 2
        val centerY = (startY + endY) / 2
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    fun setColor(color: Int) {
        paint.color = color
    }

    fun setStrokeWidth(width: Float) {
        paint.strokeWidth = width
    }
}