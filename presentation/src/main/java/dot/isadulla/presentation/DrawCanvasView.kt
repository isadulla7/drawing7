package dot.isadulla.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.example.presentation.R
import dot.isaulla.tools.Tool


class DrawCanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var bitmap: Bitmap? = null
    private var bitmapCanvas: Canvas? = null
    private var previewBitmap: Bitmap? = null
    private var previewCanvas: Canvas? = null
    private var currentTool: Tool? = null
    private var cursorX = 0f
    private var cursorY = 0f
    private var showCursor = false
    private var cursorDrawable: Bitmap? = null
    private val cursorPaint = Paint().apply { isAntiAlias = true }
    private val cursorRect = RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap?.recycle()
        previewBitmap?.recycle()

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmapCanvas = Canvas(bitmap!!)
        bitmapCanvas?.drawColor(Color.WHITE)

        previewBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        previewCanvas = Canvas(previewBitmap!!)
        clearPreviewCanvas()

        setCursorDrawable(R.drawable.ic_cursor)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }
        previewBitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }

        if (showCursor) {
            cursorDrawable?.let {
                cursorRect.set(cursorX - 16f, cursorY - 16f, cursorX + 16f, cursorY + 16f)
                canvas.drawBitmap(it, null, cursorRect, cursorPaint)
            } ?: run {
                cursorPaint.color = ContextCompat.getColor(context, android.R.color.black)
                canvas.drawCircle(cursorX, cursorY, 8f, cursorPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.getX(0)
        val y = event.getY(0)

        cursorX = x
        cursorY = y

        currentTool?.let { tool ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    showCursor = true
                    clearPreviewCanvas()
                    tool.onPreview(event, previewCanvas ?: return false)
                    invalidate()
                }

                MotionEvent.ACTION_MOVE -> {
                    showCursor = true
                    clearPreviewCanvas()
                    tool.onPreview(event, previewCanvas ?: return false)
                    invalidate()
                }

                MotionEvent.ACTION_UP -> {
                    showCursor = false
                    clearPreviewCanvas()

                    // Save current state for undo
                    bitmap?.copy(Bitmap.Config.ARGB_8888, true)?.let {
                        //undoStack.push(it)
                    }

                    tool.onCommit(bitmapCanvas ?: return false)
                    invalidate()
                }
            }
        }

        return true
    }

    fun setDrawState(state: DrawState) {
        currentTool = state.currentTool
        currentTool?.apply {
            setColor(state.currentColor)
            setStrokeWidth(state.strokeWidth)
        }
        invalidate()
    }

    fun setCursorDrawable(resId: Int) {
        cursorDrawable?.recycle()
        cursorDrawable = BitmapFactory.decodeResource(resources, resId)
        invalidate()
    }

    private fun clearPreviewCanvas() {
        previewCanvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    }

}