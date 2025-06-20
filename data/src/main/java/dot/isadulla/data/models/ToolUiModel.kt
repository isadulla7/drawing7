package dot.isadulla.data.models

import dot.isadulla.presentation.ToolType

data class ToolUiModel(
    val type: dot.isadulla.presentation.ToolType,
    val name: String,
    val iconResId: Int
)
