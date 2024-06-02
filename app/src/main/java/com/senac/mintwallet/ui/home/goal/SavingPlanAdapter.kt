package com.senac.mintwallet.ui.home.goal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.senac.mintwallet.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class SavingPlanAdapter(
    private val context: Context,
    private val savingPlans: List<SavingPlan>,
    private val onItemClick: (SavingPlan) -> Unit
) : RecyclerView.Adapter<SavingPlanAdapter.SavingPlanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingPlanViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_saving_plan, parent, false)
        return SavingPlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavingPlanViewHolder, position: Int) {
        val savingPlan = savingPlans[position]
        holder.primaryText.text = savingPlan.title

        if (savingPlan.amount != 0.0)
            holder.secondaryText.text = formatCurrency(savingPlan.amount)

        holder.leadingIcon.setImageResource(savingPlan.iconResId)
        holder.itemView.setOnClickListener {
            onItemClick(savingPlan)
        }

    }

    override fun getItemCount() = savingPlans.size

    class SavingPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val primaryText: TextView = itemView.findViewById(R.id.primary_text)
        val secondaryText: TextView = itemView.findViewById(R.id.secondary_text)
        val leadingIcon: ImageView = itemView.findViewById(R.id.leading_icon)
    }
    private fun formatCurrency(value: Double): String {
        val symbols = DecimalFormatSymbols(Locale("pt", "BR"))
        symbols.decimalSeparator = ','
        symbols.groupingSeparator = '.'

        val decimalFormat = DecimalFormat("R$ #,##0.00", symbols)

        return decimalFormat.format(value)
    }
}
