package com.senac.mintwallet.ui.home.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentHomeBinding
import com.senac.mintwallet.model.ETypeTransfer


class Home: Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavigation(view)
        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES
        requireActivity().window.statusBarColor = resources.getColor(if (isNightMode) R.color.secondary_default else R.color.background)
        binding.fab.setOnClickListener {
            val view: View = layoutInflater.inflate(R.layout.dialog_new_transfer, null)
            val dialog = BottomSheetDialog(binding.root.context)
            dialog.setContentView(view)
            dialog.show()

            val btnPayment = view.findViewById<Button>(R.id.Payment)
            btnPayment.setOnClickListener {
                dialog.dismiss()
                val action = HomeDirections.actionHomeToNewTransfer(ETypeTransfer.PAYMENT)
                findNavController().navigate(action)
            }

            val btnTransef = view.findViewById<Button>(R.id.Transef)
            btnTransef.setOnClickListener {
                dialog.dismiss()
                val action = HomeDirections.actionHomeToNewTransfer(ETypeTransfer.RECEIPT)
                findNavController().navigate(action)
            }
        }

    }

    private fun initNavigation(view: View) {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.containerHomeFrag) as NavHostFragment
        val navController = navHostFragment.navController

        val navigationView: BottomNavigationView = view.findViewById(R.id.bottomNavigationView)
        NavigationUI.setupWithNavController(navigationView, navController)
    }

}