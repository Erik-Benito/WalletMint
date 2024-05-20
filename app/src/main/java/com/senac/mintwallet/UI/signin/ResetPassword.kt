package com.senac.mintwallet.UI.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.senac.mintwallet.R
import com.senac.mintwallet.UI.CustomToast.CustomToast
import com.senac.mintwallet.databinding.FragmentSigninResetPasswordBinding

class ResetPassword: Fragment() {
    private var _binding: FragmentSigninResetPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSigninResetPasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners() {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.submitButton.setOnClickListener {
            sendEmail()

            val view: View = layoutInflater.inflate(R.layout.dialog_ok, null)
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

    private fun sendEmail() {
        val email = binding.emailEditText.text.toString().trim()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful)  {
                    CustomToast.showToast(this.requireContext(), "Falha ao enviar email de redefinição.", false)
                }
            }
    }
}