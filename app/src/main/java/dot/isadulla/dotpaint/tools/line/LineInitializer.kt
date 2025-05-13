package dot.isadulla.dotpaint.tools.line

import dot.isadulla.dotpaint.api.shape.ShapeBuilder
import dot.isadulla.dotpaint.api.shape.ShapePainter
import dot.isadulla.dotpaint.di.ServiceCenter

class LineInitializer(private val painter: ShapePainter) :
    dot.isadulla.dotpaint.api.sync.Plugin {
    override fun register() {
        ServiceCenter.set(ShapeBuilder::class, LineBuilder())
        ServiceCenter.set(ShapePainter::class, painter)
    }

    override fun id(): String = "line-tool"
}
