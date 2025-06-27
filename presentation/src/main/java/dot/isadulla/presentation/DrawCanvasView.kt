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
import dot.isaulla.tools.EraserTool
import kotlin.math.max
import kotlin.math.min

data class Shape(
    val tool: Tool, val bounds: RectF, val fillColor: Int?, // Ichki rang
    val strokeColor: Int, // Tashqi rang
    val strokeWidth: Float
)

class DrawCanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var bitmap: Bitmap? = null
    private var bitmapCanvas: Canvas? = null
    private var previewBitmap: Bitmap? = null
    private var previewCanvas: Canvas? = null
    private var currentTool: Tool? = null
    private var stateColor: Int? = null
    private var stateFillColor: Int = Color.TRANSPARENT // Ichki rang uchun
    private var stateStrokeWidth: Float? = null
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
    private var lastX = 0f
    private var lastY = 0f
    private var resizingCorner: Int? =
        null // 0: top-left, 1: top-right, 2: bottom-right, 3: bottom-left
    private val cornerRadius = 24f
    private val selectionPadding = 20f
    private val deleteButtonRadius = 24f
    private val deleteButtonOffset = 36f // Ramkadan tashqari masofa
    private var deleteButtonX = 0f
    private var deleteButtonY = 0f
    private val deleteButtonSensitivity = 1.5f
    private val deleteButtonBitmap: Bitmap? by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.trash) // Ikona resursi
    }
    private val deleteButtonRect = RectF() // Ikona chegaralari
    private val deleteButtonPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.BLUE
    }
    private val selectionPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = Color.RED
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
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
        bitmapCanvas = Canvas(bitmap!!).apply { drawColor(Color.WHITE) }
        previewBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        previewCanvas = Canvas(previewBitmap!!)
        clearPreviewCanvas()
        setCursorDrawable(R.drawable.ic_cursor)
    }

    private fun updateDeleteButtonPosition(shape: Shape) {
        val paddedBounds = RectF(shape.bounds).apply { inset(-selectionPadding, -selectionPadding) }
        deleteButtonX = paddedBounds.right + deleteButtonOffset
        deleteButtonY = paddedBounds.top - deleteButtonOffset
        deleteButtonRect.set(
            deleteButtonX - deleteButtonRadius * deleteButtonSensitivity,
            deleteButtonY - deleteButtonRadius * deleteButtonSensitivity,
            deleteButtonX + deleteButtonRadius * deleteButtonSensitivity,
            deleteButtonY + deleteButtonRadius * deleteButtonSensitivity
        )
    }

    fun setSelectedShapeStrokeWidth(width: Float) {
        selectedShape?.let { shape ->
            shape.tool.setStrokeWidth(width)
            // Shape obyekti yangilanadi
            val index = shapes.indexOf(shape)
            if (index != -1) {
                shapes[index] = Shape(
                    shape.tool,
                    shape.bounds,
                    shape.fillColor,
                    shape.strokeColor,
                    width // Yangi strokeWidth
                )
            }
            bitmap?.eraseColor(Color.WHITE)
            shapes.forEach {
                it.tool.setFillColor(it.fillColor ?: Color.TRANSPARENT)
                it.tool.setColor(it.strokeColor)
                it.tool.setStrokeWidth(it.strokeWidth)
                it.tool.onCommit(bitmapCanvas!!)
            }
            updateCanvas()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }
        previewBitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }

        selectedShape?.let { shape ->
            val paddedBounds =
                RectF(shape.bounds).apply { inset(-selectionPadding, -selectionPadding) }
            canvas.drawRect(paddedBounds, selectionPaint)
            with(paddedBounds) {
                canvas.drawCircle(left, top, cornerRadius, cornerPaint)
                canvas.drawCircle(right, top, cornerRadius, cornerPaint)
                canvas.drawCircle(right, bottom, cornerRadius, cornerPaint)
                canvas.drawCircle(left, bottom, cornerRadius, cornerPaint)
                deleteButtonX = right + deleteButtonOffset
                deleteButtonY = top - deleteButtonOffset
                deleteButtonRect.set(
                    deleteButtonX - deleteButtonRadius,
                    deleteButtonY - deleteButtonRadius,
                    deleteButtonX + deleteButtonRadius,
                    deleteButtonY + deleteButtonRadius
                )
                deleteButtonBitmap?.let {
                    canvas.drawBitmap(it, null, deleteButtonRect, null)
                }
                // Tanlangan shaklni qayta chizish
                shape.tool.setFillColor(shape.fillColor ?: Color.TRANSPARENT) // null bo‘lsa shaffof
                shape.tool.setColor(shape.strokeColor)
                shape.tool.setStrokeWidth(shape.strokeWidth)
                shape.tool.onCommit(canvas) // To‘g‘ridan-to‘g‘ri chizish
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
                        updateDeleteButtonPosition(shape)
                        if (deleteButtonRect.contains(x, y)) {
                            deleteSelectedShape()
                            return true
                        }
                        resizingCorner = getCornerAtPoint(x, y, shape.bounds)
                        if (resizingCorner != null) return true
                        isDragging = shape.bounds.contains(x, y)
                        if (isDragging) {
                            lastX = x
                            lastY = y
                            return true
                        }
                    }
                    selectedShape = shapes.lastOrNull { it.bounds.contains(x, y) }
                    if (selectedShape != null) {
                        selectedShape?.tool?.setFillColor(
                            selectedShape?.fillColor ?: Color.TRANSPARENT
                        )
                        selectedShape?.tool?.setColor(selectedShape?.strokeColor!!)
                        selectedShape?.tool?.setStrokeWidth(selectedShape?.strokeWidth!!)
                        showCursor = false
                        updateCanvas()
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
                                0 -> newBounds.set(x, y, shape.bounds.right, shape.bounds.bottom)
                                1 -> newBounds.set(shape.bounds.left, y, x, shape.bounds.bottom)
                                2 -> newBounds.set(shape.bounds.left, shape.bounds.top, x, y)
                                3 -> newBounds.set(x, shape.bounds.top, shape.bounds.right, y)
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
                            updateDeleteButtonPosition(shape)
                        }
                        bitmap?.eraseColor(Color.WHITE)
                        shapes.forEach {
                            it.tool.setFillColor(it.fillColor!!) // Ichki rang
                            it.tool.setColor(it.strokeColor) // Tashqi rang
                            it.tool.setStrokeWidth(it.strokeWidth) // Qalinlik
                            it.tool.onCommit(bitmapCanvas!!)
                        }
                        updateCanvas()
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
                resizingCorner = null
                isDragging = false
                if (!isSelectionMode) {
                    currentTool?.let { tool ->
                        tool.onPreview(
                            MotionEvent.obtain(event).apply { action = MotionEvent.ACTION_UP },
                            bitmapCanvas!!
                        )
                        if (tool !is EraserTool) {
                            val bounds = tool.getBounds()
                            val toolCopy = tool.clone()
                            toolCopy.setColor(stateColor ?: Color.BLACK)
                            toolCopy.setFillColor(stateFillColor)
                            toolCopy.setStrokeWidth(stateStrokeWidth ?: 5f)
                            shapes.add(
                                Shape(
                                    toolCopy,
                                    bounds,
                                    stateFillColor,
                                    stateColor ?: Color.BLACK,
                                    stateStrokeWidth ?: 5f
                                )
                            )
                        }
                        bitmap?.copy(Bitmap.Config.ARGB_8888, true)?.let { /*undoStack.push(it)*/ }
                        tool.onCommit(bitmapCanvas ?: return false)
                        updateCanvas()
                    }
                }
            }
        }
        return true
    }

    fun setDrawState(state: DrawState) {
        currentTool = state.currentTool
        stateColor = state.strokeColor
        stateStrokeWidth = state.strokeWidth
        currentTool?.apply {
            setColor(state.strokeColor)
            setFillColor(state.fillColor)
            setStrokeWidth(state.strokeWidth)
        }
        updateCanvas()
    }

    fun deleteSelectedShape() {
        selectedShape?.let { shape ->
            shapes.remove(shape)
            selectedShape = null
            bitmap?.eraseColor(Color.WHITE)
            shapes.forEach { it.tool.onCommit(bitmapCanvas!!) }
            updateCanvas()
        }
    }

    private fun getCornerAtPoint(x: Float, y: Float, bounds: RectF): Int? {
        with(bounds) {
            if (isPointInCircle(x, y, left, top)) return 0
            if (isPointInCircle(x, y, right, top)) return 1
            if (isPointInCircle(x, y, right, bottom)) return 2
            if (isPointInCircle(x, y, left, bottom)) return 3
        }
        return null
    }

    private fun isPointInCircle(
        x: Float, y: Float, cx: Float, cy: Float, radius: Float = cornerRadius
    ): Boolean {
        val distance = kotlin.math.hypot((x - cx).toDouble(), (y - cy).toDouble()).toFloat()
        return distance <= radius * 4
    }

    private fun updateCanvas() {
        clearPreviewCanvas()
        invalidate()
    }

    fun setSelectionMode(enabled: Boolean) {
        isSelectionMode = enabled
        selectedShape = null
        updateCanvas()
    }

    fun setCursorDrawable(resId: Int) {
        cursorDrawable?.recycle()
        cursorDrawable = BitmapFactory.decodeResource(resources, resId)
        updateCanvas()
    }

    private fun clearPreviewCanvas() {
        previewCanvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    }
}