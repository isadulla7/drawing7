package dot.isaulla.tools

import android.graphics.Canvas
import android.graphics.RectF
import android.view.MotionEvent

interface Tool {
    fun onPreview(event: MotionEvent, canvas: Canvas)
    fun onCommit(canvas: Canvas)
    fun setColor(color: Int) {}
    fun setStrokeWidth(width: Float) {}
    fun getBounds(): RectF
    fun resize(newBounds: RectF)
    fun clone(): Tool
    fun move(dx: Float, dy: Float)
}