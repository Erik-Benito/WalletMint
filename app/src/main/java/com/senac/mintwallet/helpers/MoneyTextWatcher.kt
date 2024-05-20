package com.senac.mintwallet.helpers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

class MoneyTextWatcher(private val editText: EditText) : TextWatcher {

    private val locale = Locale("pt", "BR")
    private val decimalFormatSymbols = DecimalFormatSymbols.getInstance(locale)
    private val decimalSeparator = decimalFormatSymbols.decimalSeparator.toString()

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Não é necessário fazer nada aqui
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Não é necessário fazer nada aqui
    }

    override fun afterTextChanged(s: Editable?) {
        editText.removeTextChangedListener(this)

        val cleanString = s.toString().replace("[,.]".toRegex(), "")
        val parsed: Double = try {
            cleanString.toDouble() / 100
        } catch (e: NumberFormatException) {
            0.00
        }

        val formatted = formatValue(parsed)

        editText.setText(formatted)
        editText.setSelection(formatted.length)
        editText.addTextChangedListener(this)
    }

    private fun formatValue(value: Double): String {
        val formatter = NumberFormat.getInstance(locale)
        formatter.minimumFractionDigits = 2
        return formatter.format(value)
    }
}