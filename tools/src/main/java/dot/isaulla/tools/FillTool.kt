package dot.isadulla.presentation

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import dot.isaulla.tools.Tool

class FillTool(
    private val paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = 0xFF000000.toInt() // Qora rang
    }
) : Tool {
    private var x = 0f
    private var y = 0f

    override fun onPreview(event: MotionEvent, canvas: Canvas) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            x = event.x
            y = event.y
        }
    }

    override fun onCommit(canvas: Canvas) {
        canvas.drawPaint(paint)
    }

    override fun setColor(color: Int) {
        paint.color = color
    }

    override fun setStrokeWidth(width: Float) {
        // FillTool uchun qalinlik kerak emas
    }

    override fun getBounds(): RectF {
        return RectF(x, y, x + 1f, y + 1f) // Nuqta uchun kichik bounds
    }

    override fun resize(newBounds: RectF) {
        // FillTool uchun resize kerak emas
    }

    override fun clone(): Tool {
        val newPaint = Paint().apply { set(paint) }
        return FillTool(newPaint).apply {
            x = this@FillTool.x
            y = this@FillTool.y
        }
    }

    override fun move(dx: Float, dy: Float) {
        x += dx
        y += dy
    }
}