package com.senac.mintwallet.ui.home.goal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentGoalRegisterBinding

class GoalRegister: Fragment() {
    private var _binding: FragmentGoalRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.submitButton.setOnClickListener {
            findNavController().navigate(R.id.menu_goal)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}