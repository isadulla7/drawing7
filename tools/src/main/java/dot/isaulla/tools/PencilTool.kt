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
        color = 0xFF000000.toInt()
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
                originalBounds.set(event.x, event.y, event.x + 0.1f, event.y + 0.1f)
            }

            MotionEvent.ACTION_MOVE -> {
                if (isDrawing) {
                    path.lineTo(event.x, event.y)
                    originalBounds.union(event.x, event.y)
                    canvas.drawPath(path, paint)
                }
            }

            MotionEvent.ACTION_UP -> {
                if (isDrawing) {
                    path.lineTo(event.x, event.y)
                    originalBounds.union(event.x, event.y)
                    if (originalBounds.isEmpty || originalBounds.width() < 0.1f || originalBounds.height() < 0.1f) {
                        originalBounds.set(
                            event.x - 0.1f,
                            event.y - 0.1f,
                            event.x + 0.1f,
                            event.y + 0.1f
                        )
                    }
                    isDrawing = false
                }
            }
        }
    }

    override fun onCommit(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    override fun setColor(color: Int) {
        paint.color = color
    }

    override fun setFillColor(color: Int) {

    }

    override fun setStrokeWidth(width: Float) {
        paint.strokeWidth = width
    }

    override fun getBounds(): RectF {
        val bounds = RectF()
        path.computeBounds(bounds, true)
        if (bounds.isEmpty || bounds.width() < 0.1f || bounds.height() < 0.1f) {
            bounds.set(originalBounds)
        }
        bounds.inset(-paint.strokeWidth / 2, -paint.strokeWidth / 2)
        return bounds
    }

    override fun resize(newBounds: RectF) {
        // Faqat PencilTool uchun minimal cheklov
        val safeNewBounds = RectF(newBounds).apply {
            if (width() < 0.1f) right = left + 0.1f
            if (height() < 0.1f) bottom = top + 0.1f
        }
        val oldWidth = originalBounds.width().coerceAtLeast(0.1f)
        val oldHeight = originalBounds.height().coerceAtLeast(0.1f)
        val scaleX = safeNewBounds.width() / oldWidth
        val scaleY = safeNewBounds.height() / oldHeight
        val translateX = safeNewBounds.left - originalBounds.left * scaleX
        val translateY = safeNewBounds.top - originalBounds.top * scaleY

        val newPath = Path()
        val matrix = android.graphics.Matrix()
        matrix.setScale(scaleX, scaleY)
        matrix.postTranslate(translateX, translateY)
        path.transform(matrix, newPath)
        path = newPath
        originalBounds.set(safeNewBounds)
    }

    override fun move(dx: Float, dy: Float) {
        path.offset(dx, dy)
        originalBounds.offset(dx, dy)
        if (originalBounds.isEmpty || originalBounds.width() < 0.1f || originalBounds.height() < 0.1f) {
            originalBounds.set(dx - 0.1f, dy - 0.1f, dx + 0.1f, dy + 0.1f)
        }
    }

    override fun clone(): Tool {
        val newPaint = Paint().apply { set(paint) }
        return PencilTool(newPaint).apply {
            path = Path(this@PencilTool.path)
            isDrawing = false
            originalBounds.set(this@PencilTool.originalBounds)
        }
    }
}