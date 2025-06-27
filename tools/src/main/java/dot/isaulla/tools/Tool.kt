package dot.isaulla.tools

import android.graphics.Canvas
import android.graphics.RectF
import android.view.MotionEvent

interface Tool {
    fun onPreview(event: MotionEvent, canvas: Canvas)
    fun onCommit(canvas: Canvas)
    fun setColor(color: Int) // Tashqi rang
    fun setFillColor(color: Int) // Ichki rang
    fun setStrokeWidth(width: Float)
    fun getBounds(): RectF
    fun resize(newBounds: RectF)
    fun move(dx: Float, dy: Float)
    fun clone(): Tool
}