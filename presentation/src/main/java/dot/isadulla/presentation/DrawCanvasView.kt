package dot.isadulla.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.createBitmap
import dot.isaulla.tools.Tool


class DrawCanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var bitmap: Bitmap? = null
    private var bitmapCanvas: Canvas? = null
    private var currentTool: Tool? = null
    private var paths: MutableList<Pair<Path, Tool>> = mutableListOf()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap?.recycle()
        bitmap = createBitmap(w, h)
        bitmapCanvas = Canvas(bitmap!!)
        bitmapCanvas?.drawColor(0xFFFFFFFF.toInt()) // Oq fon
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        currentTool?.let { tool ->
            tool.onTouchEvent(event, bitmapCanvas ?: return false)
            if (event.action == MotionEvent.ACTION_UP) {
                paths.add(Pair(Path(), tool))
            }
        }
        invalidate()
        return true
    }

    fun setDrawState(state: DrawState) {
        currentTool = state.currentTool
        paths = state.paths.toMutableList()
        currentTool?.let { tool ->
            try {
                val setColorMethod = tool::class.java.getMethod("setColor", Int::class.java)
                setColorMethod.invoke(tool, state.currentColor)
            } catch (e: NoSuchMethodException) {
                // Rang o'rnatish imkonsiz (masalan, EraserTool)
            }
            try {
                val setStrokeWidthMethod =
                    tool::class.java.getMethod("setStrokeWidth", Float::class.java)
                setStrokeWidthMethod.invoke(tool, state.strokeWidth)
            } catch (e: NoSuchMethodException) {
                // Chiziq qalinligi o'rnatish imkonsiz
            }
        }
        invalidate()
    }
}