import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import dot.isaulla.tools.Tool
import java.lang.Math.hypot
import kotlin.math.min

class CircleTool(
    private val strokePaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = Color.BLACK
    },
    private val fillPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.TRANSPARENT // Dastlab shaffof
    }
) : Tool {
    private var startX = 0f
    private var startY = 0f
    private var endX = 0f
    private var endY = 0f
    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f

    override fun onPreview(event: MotionEvent, canvas: Canvas) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                endX = event.x
                endY = event.y
                updateCircleParams()
                drawCircle(canvas)
            }
            MotionEvent.ACTION_UP -> {
                endX = event.x
                endY = event.y
                updateCircleParams()
            }
        }
    }

    override fun onCommit(canvas: Canvas) {
        if (fillPaint.color != Color.TRANSPARENT) {
            canvas.drawCircle(centerX, centerY, radius, fillPaint)
        }
        canvas.drawCircle(centerX, centerY, radius, strokePaint)
    }
    private fun drawCircle(canvas: Canvas) {
        if (fillPaint.color != Color.TRANSPARENT) {
            canvas.drawCircle(centerX, centerY, radius, fillPaint)
        }
        canvas.drawCircle(centerX, centerY, radius, strokePaint)
    }

    override fun setColor(color: Int) {
        strokePaint.color = color // Faqat tashqi rang
    }

    override fun setFillColor(color: Int) {
        fillPaint.color = color
    }

    override fun setStrokeWidth(width: Float) {
        strokePaint.strokeWidth = width
    }

    override fun getBounds(): RectF {
        return RectF(
            centerX - radius - strokePaint.strokeWidth / 2,
            centerY - radius - strokePaint.strokeWidth / 2,
            centerX + radius + strokePaint.strokeWidth / 2,
            centerY + radius + strokePaint.strokeWidth / 2
        )
    }

    override fun resize(newBounds: RectF) {
        centerX = newBounds.centerX()
        centerY = newBounds.centerY()
        radius = min(newBounds.width(), newBounds.height()) / 2
        startX = centerX - radius
        startY = centerY - radius
        endX = centerX + radius
        endY = centerY + radius
    }

    override fun move(dx: Float, dy: Float) {
        startX += dx
        startY += dy
        endX += dx
        endY += dy
        centerX += dx
        centerY += dy
    }

    override fun clone(): Tool {
        val newStrokePaint = Paint().apply {
            set(strokePaint)
            color = strokePaint.color // Joriy rangni nusxalash
        }
        val newFillPaint = Paint().apply {
            set(fillPaint)
            color = fillPaint.color // Joriy rangni nusxalash
        }
        return CircleTool(newStrokePaint, newFillPaint).apply {
            startX = this@CircleTool.startX
            startY = this@CircleTool.startY
            endX = this@CircleTool.endX
            endY = this@CircleTool.endY
            radius = this@CircleTool.radius
            centerX = this@CircleTool.centerX
            centerY = this@CircleTool.centerY
        }
    }

    private fun updateCircleParams() {
        radius = hypot((endX - startX).toDouble(), (endY - startY).toDouble()).toFloat() / 2
        centerX = (startX + endX) / 2
        centerY = (startY + endY) / 2
    }
}