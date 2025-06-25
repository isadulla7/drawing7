package dot.isadulla.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.presentation.R
import dot.isaulla.tools.ToolType
import dot.isaulla.tools.ToolsFactory

class DrawViewModel : ViewModel() {
    private val _drawState = MutableLiveData(DrawState())
    val drawState: LiveData<DrawState> = _drawState
    private var _isSelectionMode = false
    val isSelectionMode: Boolean get() = _isSelectionMode

    fun setSelectionMode(enabled: Boolean) {
        _isSelectionMode = enabled
    }
    fun setToolType(toolType: ToolType) {
        val newTool = ToolsFactory.createTool(toolType)
        _drawState.value = _drawState.value?.copy(
            currentToolType = toolType,
            currentTool = newTool
        )
    }

    fun setColor(color: Int) {
        _drawState.value = _drawState.value?.copy(currentColor = color)
    }

    fun setStrokeWidth(width: Float) {
        _drawState.value = _drawState.value?.copy(strokeWidth = width)
    }

    fun getToolUiList(): List<ToolUiModel> {
        return listOf(
            ToolUiModel(ToolType.PENCIL, "Pencil", R.drawable.pen),
            ToolUiModel(ToolType.CIRCLE, "Circle", R.drawable.circle),
            ToolUiModel(ToolType.RECTANGLE, "Rectangle", R.drawable.rectangle),
            ToolUiModel(ToolType.FILL, "Fill Color", R.drawable.color),
            ToolUiModel(ToolType.LINE, "Line", R.drawable.line),
            ToolUiModel(ToolType.ERASER, "Eraser", R.drawable.eraser),
        )
    }
}