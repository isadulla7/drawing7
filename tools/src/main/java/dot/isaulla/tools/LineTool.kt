package dot.isaulla.tools

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent


class LineTool(
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
    private var path = Path()

    override fun onPreview(event: MotionEvent, canvas: Canvas) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                path = Path()
                path.moveTo(startX, startY)
            }

            MotionEvent.ACTION_MOVE -> {
                endX = event.x
                endY = event.y
                path.reset()
                path.moveTo(startX, startY)
                path.lineTo(endX, endY)
                canvas.drawPath(path, paint)
            }
        }
    }

    override fun onCommit(canvas: Canvas) {
        path.reset()
        path.moveTo(startX, startY)
        path.lineTo(endX, endY)
        canvas.drawPath(path, paint)
    }

    override fun setColor(color: Int) {
        paint.color = color
    }

    override fun setStrokeWidth(width: Float) {
        paint.strokeWidth = width
    }
}