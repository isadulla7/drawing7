package dot.isadulla.dotpaint

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

        viewModel.isSelectionMode.observe(this) { mode ->
            binding.drawCanvasView.setSelectionMode(mode)
        }
        val tools = viewModel.getToolUiList()
        val adapter = ToolAdapter(tools, { selectedToolType ->
            viewModel.setToolType(selectedToolType)
        }, viewModel.isSelectionMode.value ?: false)

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter

        // isSelectionMode o‘zgarishini kuzatish
        viewModel.isSelectionMode.observe(this) { mode ->
            binding.drawCanvasView.setSelectionMode(mode)
            adapter.notifyDataSetChanged() // UI yangilash
        }

        binding.fillColorButton.setOnClickListener {
            showColorPicker { color -> viewModel.setFillColor(color) }
        }
        binding.strokeColorButton.setOnClickListener {
            showColorPicker { color -> viewModel.setStrokeColor(color) }
        }

        // Stroke width tanlash
        binding.strokeWidthSlider.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val strokeWidth = progress.toFloat().coerceAtLeast(1f)
                    viewModel.setStrokeWidth(strokeWidth)
                    binding.drawCanvasView.setSelectedShapeStrokeWidth(strokeWidth) // Tanlangan shakl uchun
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

    }

    private fun showColorPicker(onColorSelected: (Int) -> Unit) {
        // Oddiy misol uchun, haqiqiy loyihada ColorPicker kutubxonasi ishlatiladi
        val colors = intArrayOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.BLACK)
        AlertDialog.Builder(this)
            .setItems(colors.map { it.toString() }.toTypedArray()) { _, which ->
                onColorSelected(colors[which])
            }
            .show()
    }
}