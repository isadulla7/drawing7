package dot.isaulla.tools

import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent

class FillTool(
    private val paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = 0xFF000000.toInt() // Qora rang
    }
) : Tool {
    override fun onPreview(event: MotionEvent, canvas: Canvas) {
        // FillTool uchun preview kerak emas
    }

    override fun onCommit(canvas: Canvas) {
        canvas.drawPaint(paint)
    }

    override fun setColor(color: Int) {
        paint.color = color
    }
}