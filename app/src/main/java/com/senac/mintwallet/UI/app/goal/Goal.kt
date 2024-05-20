package com.senac.mintwallet.UI.app.goal

import GoalsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.senac.mintwallet.R
import com.senac.mintwallet.UI.app.goal.GoalEntity

class Goal : Fragment() {

    private lateinit var goalsRecyclerView: RecyclerView
    private lateinit var goalsAdapter: GoalsAdapter
    private lateinit var goalsList: MutableList<GoalEntity>
    private lateinit var btnContinue: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_goal, container, false)

        goalsRecyclerView = view.findViewById(R.id.goalsRecyclerView)
        btnContinue = view.findViewById(R.id.btnContinue)
        progressBar = view.findViewById(R.id.progressBar)
        progressText = view.findViewById(R.id.progressText)

        goalsList = mutableListOf(
            GoalEntity("House", R.drawable.confet_icon),
        )


        goalsAdapter = GoalsAdapter(goalsList, requireContext())
        goalsRecyclerView.layoutManager = GridLayoutManager(context, 2)
        goalsRecyclerView.adapter = goalsAdapter


        updateProgress(1, 4)

        btnContinue.setOnClickListener {
        }

        return view
    }

    private fun updateProgress(currentStep: Int, totalSteps: Int) {
        val progress = (currentStep.toFloat() / totalSteps * 100).toInt()
        progressBar.progress = progress
        progressText.text = "$currentStep/$totalSteps"
    }
}
