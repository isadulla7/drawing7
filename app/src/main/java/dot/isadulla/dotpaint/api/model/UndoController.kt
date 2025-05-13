package dot.isadulla.dotpaint.api.model


interface UndoController {
    fun addAction(action: DrawingAction)
    fun undo(): DrawingAction?
    fun redo(): DrawingAction?
    fun canUndo(): Boolean
    fun canRedo(): Boolean
}