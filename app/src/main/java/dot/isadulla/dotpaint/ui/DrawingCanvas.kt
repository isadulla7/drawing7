package dot.isadulla.dotpaint.ui
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import dot.isadulla.dotpaint.core.model.DrawingState

class DrawingCanvas @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    lateinit var engine: DrawingEngine
    lateinit var state: DrawingState

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (::engine.isInitialized) {
            engine.render(canvas)
        }
    }
}