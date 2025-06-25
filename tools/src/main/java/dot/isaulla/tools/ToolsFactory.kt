package dot.isaulla.tools

import dot.isadulla.presentation.CircleTool
import dot.isadulla.presentation.EraserTool
import dot.isadulla.presentation.FillTool
import dot.isadulla.presentation.PencilTool
import dot.isadulla.presentation.RectangleTool


object ToolsFactory {
    fun createTool(toolType: ToolType): Tool {
        return when (toolType) {
           ToolType.PENCIL -> PencilTool()
           ToolType.ERASER -> EraserTool()
           ToolType.LINE -> LineTool()
           ToolType.RECTANGLE -> RectangleTool()
           ToolType.CIRCLE -> CircleTool()
           ToolType.FILL -> FillTool()
        }
    }
}