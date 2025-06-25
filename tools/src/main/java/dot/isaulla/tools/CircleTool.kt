package dot.isadulla.presentation

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import dot.isaulla.tools.Tool
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

    override fun getBounds(): RectF {
        val radius = hypot((endX - startX).toDouble(), (endY - startY).toDouble()).toFloat() / 2
        val centerX = (startX + endX) / 2
        val centerY = (startY + endY) / 2
        return RectF(
            centerX - radius - paint.strokeWidth / 2,
            centerY - radius - paint.strokeWidth / 2,
            centerX + radius + paint.strokeWidth / 2,
            centerY + radius + paint.strokeWidth / 2
        )
    }

    override fun resize(newBounds: RectF) {
        startX = newBounds.left
        startY = newBounds.top
        endX = newBounds.right
        endY = newBounds.bottom
    }

    override fun clone(): Tool {
        val newPaint = Paint().apply { set(paint) }
        return CircleTool(newPaint).apply {
            startX = this@CircleTool.startX
            startY = this@CircleTool.startY
            endX = this@CircleTool.endX
            endY = this@CircleTool.endY
        }
    }
    override fun move(dx: Float, dy: Float) {
        startX += dx
        startY += dy
        endX += dx
        endY += dy
    }
}