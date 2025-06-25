package dot.isadulla.presentation

import android.graphics.Path
import dot.isaulla.tools.Tool
import dot.isaulla.tools.ToolType

data class DrawState(
    val currentToolType: ToolType = ToolType.PENCIL,
    val currentTool: Tool = PencilTool(),
    val currentColor: Int = 0xFF000000.toInt(), // Qora rang
    val strokeWidth: Float = 5f,
    val paths: List<Pair<Path, Tool>> = emptyList() // Chizmalarni saqlash
)
