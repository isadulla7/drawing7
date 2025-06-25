package dot.isadulla.presentation

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.MotionEvent
import dot.isaulla.tools.Tool
import android.graphics.Color

class EraserTool(
    private val paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 10f
        color = Color.WHITE // Oq fonda o‘chirish
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }
) : Tool {
    private var path = Path()
    private var originalBounds = RectF()

    override fun onPreview(event: MotionEvent, canvas: Canvas) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path = Path()
                path.moveTo(event.x, event.y)
                originalBounds.set(event.x, event.y, event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(event.x, event.y)
                originalBounds.union(event.x, event.y)
                canvas.drawPath(path, paint)
            }
        }
    }

    override fun onCommit(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    override fun setColor(color: Int) {
        // O‘chirgich uchun rang o‘zgartirish kerak emas
    }

    override fun setStrokeWidth(width: Float) {
        paint.strokeWidth = width
    }

    override fun getBounds(): RectF {
        val bounds = RectF()
        path.computeBounds(bounds, true)
        bounds.inset(-paint.strokeWidth / 2, -paint.strokeWidth / 2)
        return bounds
    }

    override fun resize(newBounds: RectF) {
        // O‘chirgich uchun resize kerak emas
    }

    override fun clone(): Tool {
        val newPaint = Paint().apply { set(paint) }
        return EraserTool(newPaint).apply {
            path = Path(this@EraserTool.path)
            originalBounds.set(this@EraserTool.originalBounds)
        }
    }

    override fun move(dx: Float, dy: Float) {
        path.offset(dx, dy)
        originalBounds.offset(dx, dy)
    }
}