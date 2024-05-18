package com.senac.mintwallet.UI.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.senac.mintwallet.UI.CustomToast.CustomToast
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
        binding.submitButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()
            if(validateForm(name, email, password, confirmPassword)) {
                registerAccount(email, password)
            } else {
                CustomToast.showToast(this.requireContext(), "Dados não conferem",false)
            }
        }
    }

    private fun validateForm(name: String, email: String, password: String, confirmPassword: String): Boolean{
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            CustomToast.showToast(this.requireContext(), "Por favor, preencha todos os campos.",false)
            return false
        }
        if (password != confirmPassword) {
            CustomToast.showToast(this.requireContext(), "As senhas não coincidem.",false)
            return false
        }
        return true
    }

    private fun registerAccount(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) {task ->
                if(task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            CustomToast.showToast(this.requireContext(), "Verificação de email enviada.", false)
                        }
                    }
                }
                else {
                    CustomToast.showToast(this.requireContext(), "Falha no cadastro: ${task.exception?.message}",false)
                }
        }

    }
}