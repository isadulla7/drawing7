package dot.isadulla.dotpaint.api.shape

import android.view.MotionEvent

interface ShapeBuilder {
    fun onTouchEvent(event: MotionEvent): Boolean
    fun build(): Shape?
    fun reset()
}