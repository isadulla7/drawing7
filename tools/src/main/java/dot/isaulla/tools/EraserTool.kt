package dot.isaulla.tools

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.view.MotionEvent


class EraserTool(
    private val paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 10f
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) // O'chirish uchun
    }
) : Tool {
    private val path = Path()

    override fun onTouchEvent(event: MotionEvent, canvas: Canvas) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(event.x, event.y)
                canvas.drawPath(path, paint)
            }
            MotionEvent.ACTION_UP -> {
                // O'chirish tugadi
            }
        }
    }

    fun setStrokeWidth(width: Float) {
        paint.strokeWidth = width
    }
}