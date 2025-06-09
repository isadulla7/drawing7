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
    override fun onTouchEvent(event: MotionEvent, canvas: Canvas) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Butun canvasni to'ldirish (kelajakda nuqtadan to'ldirish qo'shiladi)
                canvas.drawPaint(paint)
            }
        }
    }

    fun setColor(color: Int) {
        paint.color = color
    }
}