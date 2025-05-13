package dot.isadulla.dotpaint.tools.undo

import dot.isadulla.dotpaint.api.model.DrawingAction
import dot.isadulla.dotpaint.api.model.UndoController

class UndoManager : UndoController {
    private val undoStack = ArrayDeque<DrawingAction>()
    private val redoStack = ArrayDeque<DrawingAction>()

    override fun addAction(action: DrawingAction) {
        undoStack.addLast(action)
        redoStack.clear()
    }

    override fun undo(): DrawingAction? {
        val action = undoStack.removeLastOrNull()
        if (action != null) {
            redoStack.addLast(action)
        }
        return action
    }

    override fun redo(): DrawingAction? {
        val action = redoStack.removeLastOrNull()
        if (action != null) {
            undoStack.addLast(action)
        }
        return action
    }

    override fun canUndo(): Boolean = undoStack.isNotEmpty()
    override fun canRedo(): Boolean = redoStack.isNotEmpty()
}

