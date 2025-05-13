package dot.isadulla.dotpaint.core.model

import dot.isadulla.dotpaint.api.shape.Shape
import java.util.concurrent.CopyOnWriteArrayList

class DrawingState {
    val shapes: MutableList<dot.isadulla.dotpaint.api.shape.Shape> = CopyOnWriteArrayList()
    val selectedShapeIds: MutableSet<String> = mutableSetOf()

    fun clear() {
        shapes.clear()
        selectedShapeIds.clear()
    }

    fun findShapeById(id: String): dot.isadulla.dotpaint.api.shape.Shape? = shapes.find { it.id == id }
}