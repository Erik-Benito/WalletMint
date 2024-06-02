package com.senac.mintwallet.ui.home.goal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentGoalAmountStepBinding
import com.senac.mintwallet.helpers.MoneyTextWatcher
import com.senac.mintwallet.ui.component.toast.toast
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class GoalAmountStep: Fragment() {
    private var _binding: FragmentGoalAmountStepBinding? = null
    private val binding get() = _binding!!

    private val args: GoalAmountStepArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalAmountStepBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListens()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListens(){
        binding.backButton.setOnClickListener{ findNavController().popBackStack() }
        binding.amount.addTextChangedListener(MoneyTextWatcher(binding.amount))
        binding.continueButton.setOnClickListener {
            val regex = Regex("^\\d{1,3}(\\.\\d{3})*,\\d{2}$")
            if (binding.amount.text.toString().isEmpty() || !regex.matches(binding.amount.text.toString())) {
                toast.showToast(this.requireContext(), getString(R.string.message_amount_required), false)
                return@setOnClickListener
            }

            args.goal.amountMonthly = onlyNumber(binding.amount.text.toString())
            val action = GoalAmountStepDirections.actionGoalAmountStepToGoalTarget(args.goal)
            findNavController().navigate(action)
        }
    }

    private fun onlyNumber(amount: String): Double {
        val symbols = DecimalFormatSymbols(Locale("pt", "BR"))
        symbols.decimalSeparator = ','
        symbols.groupingSeparator = '.'

        val decimalFormat = DecimalFormat("#,##0.00", symbols)

        return try {
            decimalFormat.parse(amount)!!.toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
            0.0
        }
    }
}