package com.senac.mintwallet.ui.home.goal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentGoalBinding
import com.senac.mintwallet.model.GoalEntity

class Goal: Fragment() {
    private var _binding: FragmentGoalBinding? = null
    private val binding get() = _binding!!
    private lateinit var optionsAdapter: SavingPlanAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        val options = listOf(
            SavingPlan("Casa", 0.0, R.drawable.home),
            SavingPlan( "Viagem", 0.0, R.drawable.vacations),
            SavingPlan("Carro", 0.0, R.drawable.car ),
            SavingPlan("Casamento", 0.0, R.drawable.wedding),
        )

        optionsAdapter = SavingPlanAdapter(this.requireContext(), options) {
            savingPlan ->
                val goal: GoalEntity = GoalEntity()
                goal.type = savingPlan.title
                val action = GoalDirections.actionMenuGoalToGoalAmountStep(goal)
                findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(this.requireContext(), 2)
        binding.recyclerView.adapter = optionsAdapter

        binding.backButton.setOnClickListener{ findNavController().popBackStack() }
    }
}