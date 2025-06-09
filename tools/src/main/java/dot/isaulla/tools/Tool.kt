package dot.isaulla.tools

import android.graphics.Canvas
import android.view.MotionEvent

interface Tool {
    fun onTouchEvent(event: MotionEvent, canvas: Canvas)
}