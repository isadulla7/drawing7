package dot.isadulla.presentation

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import dot.isaulla.tools.Tool
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
    private var endX = 0f
    private var endY = 0f
    private var rect = RectF()

    override fun onPreview(event: MotionEvent, canvas: Canvas) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                endX = event.x
                endY = event.y
                rect.set(min(startX, endX), min(startY, endY), max(startX, endX), max(startY, endY))
                canvas.drawRect(rect, paint)
            }
        }
    }

    override fun onCommit(canvas: Canvas) {
        rect.set(min(startX, endX), min(startY, endY), max(startX, endX), max(startY, endY))
        canvas.drawRect(rect, paint)
    }

    override fun setColor(color: Int) {
        paint.color = color
    }

    override fun setFillColor(color: Int) {

    }

    override fun setStrokeWidth(width: Float) {
        paint.strokeWidth = width
    }

    override fun getBounds(): RectF = RectF(rect).apply {
        inset(-paint.strokeWidth / 2, -paint.strokeWidth / 2)
    }

    override fun resize(newBounds: RectF) {
        rect.set(newBounds)
        startX = newBounds.left
        startY = newBounds.top
        endX = newBounds.right
        endY = newBounds.bottom
    }

    override fun clone(): Tool {
        val newPaint = Paint().apply { set(paint) }
        return RectangleTool(newPaint).apply {
            startX = this@RectangleTool.startX
            startY = this@RectangleTool.startY
            endX = this@RectangleTool.endX
            endY = this@RectangleTool.endY
            rect.set(this@RectangleTool.rect)
        }
    }

    override fun move(dx: Float, dy: Float) {
        rect.offset(dx, dy)
        startX += dx
        startY += dy
        endX += dx
        endY += dy
    }
}