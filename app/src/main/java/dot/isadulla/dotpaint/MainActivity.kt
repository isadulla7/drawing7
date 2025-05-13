package dot.isadulla.dotpaint

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dot.isadulla.dotpaint.api.shape.ShapePainter
import dot.isadulla.dotpaint.api.sync.Plugin
import dot.isadulla.dotpaint.core.model.DrawingState
import dot.isadulla.dotpaint.di.ServiceCenter
import dot.isadulla.dotpaint.ui.DrawingCanvas
import dot.isadulla.dotpaint.ui.DrawingEngine
import dot.isadulla.dotpaint.tools.line.LineInitializer
import dot.isadulla.dotpaint.tools.line.LinePainter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val canvasView = DrawingCanvas(this)
        val state = DrawingState()
        val paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 5f
            style = Paint.Style.STROKE
        }

        // Line plugin init
        val plugin: Plugin = LineInitializer(LinePainter(paint))
        plugin.register()

        // Set up engine and state
        val painter = ServiceCenter.get<ShapePainter>()
        val engine = DrawingEngine(state, painter)

        canvasView.engine = engine
        canvasView.state = state

        setContentView(canvasView)
    }
}