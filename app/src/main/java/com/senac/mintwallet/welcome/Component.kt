package com.senac.mintwallet.welcome

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES

        requireActivity().window.statusBarColor = resources.getColor(R.color.green);
        requireActivity().window.navigationBarColor = resources.getColor(if(isNightMode) R.color.black else R.color.white);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}