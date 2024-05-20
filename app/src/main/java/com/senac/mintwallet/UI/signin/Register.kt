package com.senac.mintwallet.UI.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.senac.mintwallet.R
import com.senac.mintwallet.UI.CustomToast.CustomToast
import com.senac.mintwallet.databinding.FragmentSigninRegisterBinding
import com.senac.mintwallet.model.Repository

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
            if(!validateForm(name, email, password, confirmPassword)) {
                CustomToast.showToast(this.requireContext(), "Dados não conferem",false)
                return@setOnClickListener
            }

            registerAccount(name, email, password)
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

    private fun createRepoUser(name: String, email: String) {
        val user = FirebaseAuth.getInstance().currentUser

        Repository().create(name, email)

        user?.sendEmailVerification()?.addOnCompleteListener {task ->
            if (task.isSuccessful) {
                val view: View = layoutInflater.inflate(R.layout.dialog_ok, null)
                view.findViewById<TextView>(R.id.description_pop_card).text =
                    getString(R.string.SendEmailConfirm)

                val dialog = BottomSheetDialog(binding.root.context)
                dialog.setContentView(view)
                dialog.show()

                val buttonOK = view.findViewById<Button>(R.id.btn_done)
                buttonOK.setOnClickListener {
                    dialog.dismiss()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun registerAccount(name: String, email: String, password: String) {
        val auth = FirebaseAuth.getInstance()

        auth
         .createUserWithEmailAndPassword(email, password)
         .addOnCompleteListener(requireActivity()) {task ->
            if(!task.isSuccessful) {
                val message =
                    when(task.exception){
                        is FirebaseAuthUserCollisionException -> getString(R.string.email_already_in_use);
                        is FirebaseAuthWeakPasswordException -> getString(R.string.weak_password);
                        is FirebaseAuthInvalidCredentialsException -> getString(R.string.invalid_email);
                        else -> getString(R.string.unhandlerError);
                    }
                CustomToast.showToast(this.requireContext(), message,false)

                return@addOnCompleteListener;
            }

             this.createRepoUser(email, password)
        }
    }
}