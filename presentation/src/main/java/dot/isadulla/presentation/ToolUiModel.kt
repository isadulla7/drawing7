package dot.isadulla.presentation

import dot.isaulla.tools.ToolType

data class ToolUiModel(
    val type: ToolType,
    val name: String,
    val iconResId: Int
)
