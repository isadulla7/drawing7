package dot.isadulla.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import dot.isaulla.tools.Tool
import com.example.presentation.R
import java.lang.Math.hypot
import kotlin.math.max
import kotlin.math.min

data class Shape(
    val tool: Tool, val bounds: RectF, val color: Int, val strokeWidth: Float
)

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
    private val shapes = mutableListOf<Shape>()
    private var selectedShape: Shape? = null
    private var isSelectionMode = false
    private var isDragging = false
    private var lastX = 0f // Boshlang‘ich kursor x
    private var lastY = 0f // Boshlang‘ich kursor y
    private var resizingCorner: Int? =
        null // 0: top-left, 1: top-right, 2: bottom-right, 3: bottom-left
    private val cornerRadius = 16f // Burchak nuqtalarining kengligini oshirish
    private val selectionPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = Color.RED
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f) // Uzuk-uzuk chiziq
    }
    private val cornerPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.RED
    }

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

        // Tanlangan shaklni uzuk-uzuk to‘rtburchak va burchak nuqtalari bilan chizish
        selectedShape?.let { shape ->
            canvas.drawRect(shape.bounds, selectionPaint)
            // Burchak nuqtalari
            with(shape.bounds) {
                canvas.drawCircle(left, top, cornerRadius, cornerPaint) // Top-left
                canvas.drawCircle(right, top, cornerRadius, cornerPaint) // Top-right
                canvas.drawCircle(right, bottom, cornerRadius, cornerPaint) // Bottom-right
                canvas.drawCircle(left, bottom, cornerRadius, cornerPaint) // Bottom-left
            }
        }

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

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isSelectionMode) {
                    selectedShape?.let { shape ->
                        resizingCorner = getCornerAtPoint(x, y, shape.bounds)
                        if (resizingCorner != null) return true
                        isDragging = shape.bounds.contains(x, y)
                        if (isDragging) {
                            lastX = x
                            lastY = y
                            return true
                        }
                    }
                    selectedShape = shapes.find { it.bounds.contains(x, y) }
                    if (selectedShape != null) {
                        showCursor = false
                        clearPreviewCanvas()
                        invalidate()
                        return true
                    }
                } else {
                    currentTool?.let { tool ->
                        showCursor = true
                        clearPreviewCanvas()
                        tool.onPreview(event, previewCanvas ?: return false)
                        invalidate()
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isSelectionMode && selectedShape != null) {
                    selectedShape?.let { shape ->
                        if (resizingCorner != null) {
                            val newBounds = RectF(shape.bounds)
                            when (resizingCorner) {
                                0 -> newBounds.set(
                                    min(x, shape.bounds.right),
                                    min(y, shape.bounds.bottom),
                                    max(x, shape.bounds.right),
                                    max(y, shape.bounds.bottom)
                                )

                                1 -> newBounds.set(
                                    shape.bounds.left,
                                    min(y, shape.bounds.bottom),
                                    max(x, shape.bounds.left),
                                    max(y, shape.bounds.bottom)
                                )

                                2 -> newBounds.set(
                                    shape.bounds.left,
                                    shape.bounds.top,
                                    max(x, shape.bounds.left),
                                    max(y, shape.bounds.top)
                                )

                                3 -> newBounds.set(
                                    min(x, shape.bounds.right),
                                    shape.bounds.top,
                                    max(x, shape.bounds.right),
                                    max(y, shape.bounds.top)
                                )
                            }
                            shape.tool.resize(newBounds)
                            shape.bounds.set(newBounds)
                        } else if (isDragging) {
                            val dx = x - lastX
                            val dy = y - lastY
                            shape.tool.move(dx, dy)
                            shape.bounds.offset(dx, dy)
                            lastX = x
                            lastY = y
                        }
                        bitmap?.eraseColor(Color.WHITE)
                        shapes.forEach { it.tool.onCommit(bitmapCanvas!!) }
                        invalidate()
                    }
                } else if (!isSelectionMode) {
                    currentTool?.let { tool ->
                        showCursor = true
                        clearPreviewCanvas()
                        tool.onPreview(event, previewCanvas ?: return false)
                        invalidate()
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                showCursor = false
                clearPreviewCanvas()
                resizingCorner = null
                isDragging = false
                if (!isSelectionMode) {
                    currentTool?.let { tool ->
                        val bounds = tool.getBounds()
                        val toolCopy = tool.clone()
                        toolCopy.setColor(paint.color)
                        toolCopy.setStrokeWidth(paint.strokeWidth)
                        shapes.add(Shape(toolCopy, bounds, paint.color, paint.strokeWidth))
                        bitmap?.copy(Bitmap.Config.ARGB_8888, true)?.let { /*undoStack.push(it)*/ }
                        tool.onCommit(bitmapCanvas ?: return false)
                        invalidate()
                    }
                }
            }
        }
        return true
    }

    private fun getCornerAtPoint(x: Float, y: Float, bounds: RectF): Int? {
        with(bounds) {
            if (isPointInCircle(x, y, left, top)) return 0 // Top-left
            if (isPointInCircle(x, y, right, top)) return 1 // Top-right
            if (isPointInCircle(x, y, right, bottom)) return 2 // Bottom-right
            if (isPointInCircle(x, y, left, bottom)) return 3 // Bottom-left
        }
        return null
    }

    private fun isPointInCircle(x: Float, y: Float, cx: Float, cy: Float): Boolean {
        val distance = kotlin.math.hypot((x - cx).toDouble(), (y - cy).toDouble()).toFloat()
        return distance <= cornerRadius * 3 // Sezgirlikni oshirish
    }

    fun setSelectionMode(enabled: Boolean) {
        isSelectionMode = enabled
        selectedShape = null
        invalidate()
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

    private val paint: Paint
        get() = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = try {
                currentTool?.let { tool ->
                    tool::class.java.getDeclaredField("paint").apply { isAccessible = true }
                        .get(tool) as Paint
                }?.strokeWidth ?: 5f
            } catch (e: Exception) {
                5f
            }
            color = try {
                currentTool?.let { tool ->
                    tool::class.java.getDeclaredField("paint").apply { isAccessible = true }
                        .get(tool) as Paint
                }?.color ?: Color.BLACK
            } catch (e: Exception) {
                Color.BLACK
            }
        }
}