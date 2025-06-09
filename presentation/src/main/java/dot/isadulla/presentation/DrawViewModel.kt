package dot.isadulla.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dot.isadulla.domain.ToolType
import dot.isaulla.tools.ToolsFactory

class DrawViewModel : ViewModel() {
    private val _drawState = MutableLiveData(DrawState())
    val drawState: LiveData<DrawState> = _drawState

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
}