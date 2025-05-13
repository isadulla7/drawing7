package dot.isadulla.dotpaint.tools.undo

import dot.isadulla.dotpaint.api.model.DrawingAction
import dot.isadulla.dotpaint.api.model.UndoController

class UndoManager : UndoController {
    private val undoStack = ArrayDeque<DrawingAction>()
    private val redoStack = ArrayDeque<DrawingAction>()

    override fun addAction(action: DrawingAction) {
        undoStack.add(action)
        redoStack.clear()
    }

    override fun undo(): DrawingAction? {
        val action = undoStack.removeLastOrNull()
        if (action != null) {
            redoStack.add(action)
        }
        return action
    }

    override fun redo(): DrawingAction? {
        val action = redoStack.removeLastOrNull()
        if (action != null) {
            undoStack.add(action)
        }
        return action
    }

    override fun canUndo(): Boolean = undoStack.isNotEmpty()
    override fun canRedo(): Boolean = redoStack.isNotEmpty()
}

