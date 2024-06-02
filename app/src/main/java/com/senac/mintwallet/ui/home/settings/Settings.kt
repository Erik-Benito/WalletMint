package com.senac.mintwallet.ui.home.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.senac.mintwallet.MainActivity
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentSettingsBinding


class Settings : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialListens()
    }

    private fun initialListens() {
        binding.signout.setOnClickListener {
            val settings: SharedPreferences = requireContext().getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
            settings.edit().clear().apply()
            FirebaseAuth.getInstance().signOut()

            activity?.let {
                val intent = Intent(it, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                it.startActivity(intent)
                it.finishAffinity() // Para fechar todas as atividades e encerrar o aplicativo completamente
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

