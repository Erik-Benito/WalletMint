package com.senac.mintwallet

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.senac.mintwallet.databinding.FragmentSplashBinding


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

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES

        requireActivity().window.statusBarColor = resources.getColor( if(isNightMode) R.color.black else R.color.white );
        requireActivity().window.navigationBarColor = resources.getColor( if(isNightMode) R.color.black else R.color.white );

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, 2000)
    }

    private fun navigateToNextScreen() {
        findNavController().navigate(R.id.action_splash_to_main_welcome)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}