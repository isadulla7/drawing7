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
    private val _isSelectionMode = MutableLiveData(false) // LiveData qilindi
    val isSelectionMode: LiveData<Boolean> = _isSelectionMode
    private fun setSelectionMode(enabled: Boolean) {
        _isSelectionMode.value = enabled
    }

    fun setToolType(toolType: ToolType) {
        if (toolType == ToolType.SELECTION) {
            setSelectionMode(!(_isSelectionMode.value ?: false)) // Rejimni almashtirish
        } else {
            setSelectionMode(false) // Boshqa vositalar uchun tanlash rejimini o‘chirish
            val newTool = ToolsFactory.createTool(toolType)
            _drawState.value = newTool?.let {
                _drawState.value?.copy(
                    currentToolType = toolType,
                    currentTool = it
                )
            }
        }
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
            ToolUiModel(ToolType.SELECTION, "Selection", R.drawable.cursor),
        )
    }
}