package com.senac.mintwallet.ui.home.goal

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentGoalTargetBinding
import com.senac.mintwallet.ui.component.toast.toast
import java.util.Calendar

class GoalTarget: Fragment() {
    private var _binding: FragmentGoalTargetBinding? = null
    private val binding get() = _binding!!
    private val args: GoalTargetArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalTargetBinding.inflate(inflater,container,false)
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
        binding.monthPicker.setOnClickListener{
            showMonthPickerDialog()
        }
        binding.continueButton.setOnClickListener {
            if (binding.monthPicker.text.toString() == "MES" || binding.yearPicker.text.toString() == "ANO") {
                toast.showToast(this.requireContext(), getString(R.string.message_targer_required), false)
                return@setOnClickListener
            }

            args.goal.dayIndicator = getMesNome(binding.monthPicker.text.toString()).toString() + "/" + binding.yearPicker.text.toString()
            val action = GoalTargetDirections.actionGoalTargetToGoalConfirm(args.goal)
            findNavController().navigate(action)
        }
        binding.yearPicker.setOnClickListener{
            YearPickerDialog(this.requireContext(), { year ->
                binding.yearPicker.text = year.toString()
            }, 2024, 3000).show()
        }
    }

    private fun getNomeMes(number: Int): String {
        return when (number) {
            1 -> "Janeiro"
            2 -> "Fevereiro"
            3 -> "Março"
            4 -> "Abril"
            5 -> "Maio"
            6 -> "Junho"
            7 -> "Julho"
            8 -> "Agosto"
            9 -> "Setembro"
            10 -> "Outubro"
            11 -> "Novembro"
            12 -> "Dezembro"
            else -> ""
        }
    }

    private fun getMesNome(mes: String): Int {
        return when (mes) {
            "Janeiro"-> 1
            "Fevereiro"-> 2
            "Março"-> 3
            "Abril"-> 4
            "Maio"-> 5
            "Junho"-> 6
            "Julho"-> 7
            "Agosto"-> 8
            "Setembro"-> 9
            "Outubro"->10
            "Novembro"->11
            "Dezembro"->12
            else -> 0
        }
    }

    private fun showMonthPickerDialog() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1 // Adiciona 1 pois os meses começam do zero

        MonthPickerDialog(this.requireContext()) { year, month ->
            binding.monthPicker.text = getNomeMes(month)
        }
    }

    class MonthPickerDialog(
        context: Context,
        private val onDateSetListener: (year: Int, month: Int) -> Unit
    ) {

        init {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_month_picker, null)
            val monthPicker = dialogView.findViewById<NumberPicker>(R.id.month_picker)

            monthPicker.minValue = 1
            monthPicker.maxValue = 12
            monthPicker.displayedValues = arrayOf(
                "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
            )

            val alertDialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val selectedYear = Calendar.getInstance().get(Calendar.YEAR)
                    val selectedMonth = monthPicker.value
                    onDateSetListener.invoke(selectedYear, selectedMonth)
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            alertDialog.show()
        }
    }

    class YearPickerDialog(
        context: Context,
        private val onYearSetListener: (year: Int) -> Unit,
        private val startYear: Int,
        private val endYear: Int
    ) {

        private val dialog: AlertDialog

        init {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_month_picker, null)
            val yearPicker = dialogView.findViewById<NumberPicker>(R.id.month_picker)

            yearPicker.minValue = startYear
            yearPicker.maxValue = endYear

            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            yearPicker.value = currentYear

            dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val selectedYear = yearPicker.value
                    onYearSetListener.invoke(selectedYear)
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
        }

        fun show() {
            dialog.show()
        }
    }
}