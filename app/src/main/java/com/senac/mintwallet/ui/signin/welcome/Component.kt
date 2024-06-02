package com.senac.mintwallet.ui.signin.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentWelcomeComponentBinding


class Component : Fragment() {
    private var _binding: FragmentWelcomeComponentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeComponentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.statusBarColor = resources.getColor(R.color.primary_default);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}