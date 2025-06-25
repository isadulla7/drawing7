package dot.isadulla.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.R
import dot.isadulla.presentation.ToolUiModel
import dot.isaulla.tools.ToolType

class ToolAdapter(
    private val tools: List<ToolUiModel>, private val onToolSelected: (ToolType) -> Unit
) : RecyclerView.Adapter<ToolAdapter.ToolViewHolder>() {
    private var selectedToolType: ToolType? = null // Tanlangan tool

    inner class ToolViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.toolIcon)
        val name: TextView = view.findViewById(R.id.toolName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tool, parent, false)
        return ToolViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        val tool = tools[position]
        holder.icon.setImageResource(tool.iconResId)
        holder.name.text = tool.name

        holder.view.setBackgroundResource(
            if (tool.type == selectedToolType) R.drawable.selected_tool_background
            else R.drawable.default_tool_background
        )

        holder.view.setOnClickListener {
            val oldPosition = tools.indexOfFirst { it.type == selectedToolType }
            selectedToolType = tool.type
            notifyItemChanged(oldPosition)
            notifyItemChanged(position)
            onToolSelected(tool.type)
        }
    }

    override fun getItemCount() = tools.size
}