package dot.isadulla.dotpaint

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dot.isadulla.domain.ToolType
import dot.isadulla.dotpaint.databinding.ActivityMainBinding
import dot.isadulla.presentation.DrawViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: DrawViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // DrawCanvasView ni sozlash
        viewModel.drawState.observe(this) { state ->
            binding.drawCanvasView.setDrawState(state)
        }

        // Namuna: Vosita tanlash tugmalari
        binding.btnPencil.setOnClickListener { viewModel.setToolType(ToolType.PENCIL) }
        binding.btnEraser.setOnClickListener { viewModel.setToolType(ToolType.ERASER) }
        binding.btnLine.setOnClickListener { viewModel.setToolType(ToolType.LINE) }
        binding.btnRectangle.setOnClickListener { viewModel.setToolType(ToolType.RECTANGLE) }
        binding.btnCircle.setOnClickListener { viewModel.setToolType(ToolType.CIRCLE) }
        binding.btnFill.setOnClickListener { viewModel.setToolType(ToolType.FILL) }
    }
}