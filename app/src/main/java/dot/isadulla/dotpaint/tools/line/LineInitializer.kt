package dot.isadulla.dotpaint.tools.line

import dot.isadulla.dotpaint.di.ServiceCenter

class LineInitializer(private val painter: dot.isadulla.dotpaint.api.shape.ShapePainter) :
    dot.isadulla.dotpaint.api.sync.Plugin {
    override fun register() {
        ServiceCenter.set(dot.isadulla.dotpaint.api.shape.ShapeBuilder::class, LineBuilder())
        ServiceCenter.set(dot.isadulla.dotpaint.api.shape.ShapePainter::class, painter)
    }

    override fun id(): String = "line-tool"
}
