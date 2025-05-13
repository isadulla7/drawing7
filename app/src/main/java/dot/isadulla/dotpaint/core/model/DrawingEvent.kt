package dot.isadulla.dotpaint.core.model

import dot.isadulla.dotpaint.api.model.DrawingAction

sealed class DrawingEvent {
    data class ActionPerformed(val action: dot.isadulla.dotpaint.api.model.DrawingAction) : DrawingEvent()
    data object UndoTriggered : DrawingEvent()
    data object RedoTriggered : DrawingEvent()
}