package dot.isadulla.presentation

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.MotionEvent
import dot.isaulla.tools.Tool

class PencilTool(
    private val paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = 0xFF000000.toInt() // Qora rang
    }
) : Tool {
    private var path = Path()
    private var isDrawing = false
    private var originalBounds = RectF()

    override fun onPreview(event: MotionEvent, canvas: Canvas) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.reset()
                path.moveTo(event.x, event.y)
                isDrawing = true
                originalBounds.set(event.x, event.y, event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDrawing) {
                    path.lineTo(event.x, event.y)
                    originalBounds.union(event.x, event.y)
                    canvas.drawPath(path, paint)
                }
            }
        }
    }

    override fun onCommit(canvas: Canvas) {
        if (isDrawing) {
            canvas.drawPath(path, paint)
            isDrawing = false
        }
    }

    override fun setColor(color: Int) {
        paint.color = color
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
        val scaleX = newBounds.width() / originalBounds.width()
        val scaleY = newBounds.height() / originalBounds.height()
        val translateX = newBounds.left - originalBounds.left * scaleX
        val translateY = newBounds.top - originalBounds.top * scaleY

        val newPath = Path()
        val matrix = android.graphics.Matrix()
        matrix.setScale(scaleX, scaleY)
        matrix.postTranslate(translateX, translateY)
        path.transform(matrix, newPath)
        path = newPath
        originalBounds.set(newBounds)
    }

    override fun clone(): Tool {
        val newPaint = Paint().apply { set(paint) }
        return PencilTool(newPaint).apply {
            path = Path(this@PencilTool.path)
            isDrawing = this@PencilTool.isDrawing
            originalBounds.set(this@PencilTool.originalBounds)
        }
    }

    override fun move(dx: Float, dy: Float) {
        path.offset(dx, dy)
        originalBounds.offset(dx, dy)
    }
}