package dot.isaulla.tools

import dot.isadulla.domain.ToolType

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