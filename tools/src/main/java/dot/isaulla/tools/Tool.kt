package dot.isaulla.tools

import android.graphics.Canvas
import android.view.MotionEvent

interface Tool {
    fun onPreview(event: MotionEvent, canvas: Canvas)
    fun onCommit(canvas: Canvas)
    fun setColor(color: Int) {}
    fun setStrokeWidth(width: Float) {}
}