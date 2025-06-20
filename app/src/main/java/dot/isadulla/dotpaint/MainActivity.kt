package dot.isadulla.dotpaint

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dot.isadulla.presentation.ToolType
import dot.isadulla.dotpaint.databinding.ActivityMainBinding
import dot.isadulla.presentation.DrawViewModel
import dot.isadulla.presentation.adapter.ToolAdapter

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
        val tools = viewModel.getToolUiList()
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = ToolAdapter(tools) { selectedToolType ->
            viewModel.setToolType(selectedToolType)
        }
        // Namuna: Vosita tanlash tugmalari
//        binding.btnPencil.setOnClickListener { viewModel.setToolType(ToolType.PENCIL) }
//        binding.btnEraser.setOnClickListener { viewModel.setToolType(ToolType.ERASER) }
//        binding.btnLine.setOnClickListener { viewModel.setToolType(ToolType.LINE) }
//        binding.btnRectangle.setOnClickListener { viewModel.setToolType(ToolType.RECTANGLE) }
//        binding.btnCircle.setOnClickListener { viewModel.setToolType(ToolType.CIRCLE) }
//        binding.btnFill.setOnClickListener { viewModel.setToolType(ToolType.FILL) }
    }
}