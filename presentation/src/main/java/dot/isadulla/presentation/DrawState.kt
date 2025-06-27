package dot.isadulla.presentation

import android.graphics.Color
import android.graphics.Path
import dot.isaulla.tools.Tool
import dot.isaulla.tools.ToolType

data class DrawState(
    val currentTool: Tool? = null,
    val currentToolType: ToolType? = null,
    val currentColor: Int = Color.BLACK, // Eski rang
    val strokeWidth: Float = 5f,
    val fillColor: Int = Color.TRANSPARENT, // Ichki rang
    val strokeColor: Int = Color.BLACK // Tashqi rang
)
