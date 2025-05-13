package dot.isadulla.dotpaint.api.model

import android.graphics.drawable.shapes.Shape

sealed class DrawingAction {
    data class AddShape(val shape: Shape) : DrawingAction()
    data class RemoveShape(val shapeId: String) : DrawingAction()
    data class MoveShape(val shapeId: String, val dx: Float, val dy: Float) : DrawingAction()
    data class ResizeShape(val shapeId: String, val scale: Float) : DrawingAction()
    data class ChangeColor(val shapeId: String, val color: Int) : DrawingAction()
    data class Custom(val type: String, val payload: Map<String, Any>) : DrawingAction()
}