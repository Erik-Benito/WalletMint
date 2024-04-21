package com.senac.mintwallet.signin

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentSigninResetPasswordBinding

class ResetPassword: Fragment() {
    private var _binding: FragmentSigninResetPasswordBinding? = null
    private val binding get() = _binding!!

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
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.submitButton.setOnClickListener {
            val view: View = layoutInflater.inflate(R.layout.dialog_ok, null)
            val dialog = BottomSheetDialog(binding.root.context)
            dialog.setContentView(view)
            dialog.show()

            val buttonOK = view.findViewById<Button>(R.id.btn_done)
            buttonOK.setOnClickListener {
                dialog.dismiss()
            }
        }
   }
}