package com.senac.mintwallet.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentSplashBinding
import com.senac.mintwallet.ui.home.HomeActivity


class Splash : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       navigateToNextScreen()
    }

    private fun navigateToNextScreen() {
        val sharedPreferences =
            context?.getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE)!!
        val userId = sharedPreferences.getString("USER_ID", null)
        if (userId != null) {
            val intent = Intent(context, HomeActivity::class.java)
            startActivity(intent)
            return;
        }

         findNavController().navigate(R.id.action_splash_to_main_welcome_frag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

