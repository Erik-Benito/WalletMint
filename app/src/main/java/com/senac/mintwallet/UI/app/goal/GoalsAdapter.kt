import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.senac.mintwallet.R
import com.senac.mintwallet.UI.app.goal.GoalEntity

class GoalsAdapter(private val goals: List<GoalEntity>, private val context: Context) :
    RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_goal, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val goal = goals[position]
        holder.icon.setImageResource(goal.iconResourceId)
        holder.name.text = goal.name
        holder.container.isSelected = position == selectedPosition

        holder.container.setOnClickListener {
            notifyItemChanged(selectedPosition)
            selectedPosition = position
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount(): Int {
        return goals.size
    }

    inner class GoalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container: RelativeLayout = view.findViewById(R.id.container)
        val icon: ImageView = view.findViewById(R.id.icon)
        val name: TextView = view.findViewById(R.id.name)
    }
}
