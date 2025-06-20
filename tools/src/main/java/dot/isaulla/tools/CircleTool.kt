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

    override fun onPreview(event: MotionEvent, canvas: Canvas) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                endX = event.x
                endY = event.y
                val radius = hypot((endX - startX).toDouble(), (endY - startY).toDouble()).toFloat() / 2
                val centerX = (startX + endX) / 2
                val centerY = (startY + endY) / 2
                canvas.drawCircle(centerX, centerY, radius, paint)
            }
        }
    }

    override fun onCommit(canvas: Canvas) {
        val radius = hypot((endX - startX).toDouble(), (endY - startY).toDouble()).toFloat() / 2
        val centerX = (startX + endX) / 2
        val centerY = (startY + endY) / 2
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    override fun setColor(color: Int) {
        paint.color = color
    }

    override fun setStrokeWidth(width: Float) {
        paint.strokeWidth = width
    }
}