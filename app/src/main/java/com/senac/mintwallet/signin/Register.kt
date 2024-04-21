package com.senac.mintwallet.signin

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentSigninRegisterBinding

class Register: Fragment() {
    private var _binding: FragmentSigninRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSigninRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES

        requireActivity().window.statusBarColor = resources.getColor(if(isNightMode) R.color.black else R.color.white);
        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners() {
        binding.loginLink.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}