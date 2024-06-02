package com.senac.mintwallet.ui.home.goal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentGoalConfirmBinding
import com.senac.mintwallet.model.Repository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

class GoalConfirm : Fragment() {
    private var _binding: FragmentGoalConfirmBinding? = null
    private val binding get() = _binding!!
    private val args: GoalConfirmArgs by navArgs()
    private var repository: Repository = Repository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListens()
    }

    private fun initListens() {
        binding.backButton.setOnClickListener { findNavController().popBackStack() }
        binding.goal.text = args.goal.type
        binding.target.text = args.goal.dayIndicator
        binding.amountMonthly.text = formatCurrency(args.goal.amountMonthly!!)

        viewLifecycleOwner.lifecycleScope.launch {
            setLoading(true)
            val cdiData = fetchCdiData()
            val months = calculateMonthDifference(args.goal.dayIndicator!!)
            args.goal.amountTotal = calculateFutureValueWithMonthlyContributions(cdiData, args.goal.amountMonthly!!, months)
            binding.amountTotal.text = formatCurrency(args.goal.amountTotal!!)
            setLoading(false)
        }

        binding.continueButton.setOnClickListener {
            repository.createNewGoal(args.goal)
            findNavController().navigate(R.id.goalRegister)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingTextView.visibility = View.VISIBLE
            binding.titleText.visibility = View.GONE
            binding.subtitleText.visibility = View.GONE
            binding.targetAchievedLabel.visibility = View.GONE
            binding.datePicker.visibility = View.GONE
            binding.container.visibility = View.GONE
            binding.continueButton.visibility = View.GONE
        } else {
            binding.loadingTextView.visibility = View.GONE
            binding.titleText.visibility = View.VISIBLE
            binding.subtitleText.visibility = View.VISIBLE
            binding.targetAchievedLabel.visibility = View.VISIBLE
            binding.datePicker.visibility = View.VISIBLE
            binding.container.visibility = View.VISIBLE
            binding.continueButton.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Serializable
    data class CdiData(
        @SerialName("data") val date: String,
        @SerialName("valor") val value: String
    )

    suspend fun fetchCdiData(): List<CdiData> {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val url = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.12/dados?formato=json"
        val response: HttpResponse = client.get(url)
        return response.body()
    }

    fun calculateFutureValueWithMonthlyContributions(cdiData: List<CdiData>, monthlyContribution: Double, months: Int): Double {
        val dailyRate = cdiData.last().value.toDouble() / 100
        val monthlyRate = Math.pow(1 + dailyRate, 22.0) - 1 // Aproximando 22 dias úteis em um mês
        var futureValue = 0.0

        for (i in 0 until abs(months)) {
            futureValue = (futureValue + monthlyContribution) * (1 + monthlyRate)
        }

        return futureValue
    }

    private fun calculateMonthDifference(dateString: String): Int {
        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())

        val currentDate = Calendar.getInstance()

        val providedDate = Calendar.getInstance()
        providedDate.time = dateFormat.parse(dateString)!!

        val diffYear = currentDate.get(Calendar.YEAR) - providedDate.get(Calendar.YEAR)
        val diffMonth = diffYear * 12 + currentDate.get(Calendar.MONTH) - providedDate.get(Calendar.MONTH)

        return diffMonth
    }

    private fun formatCurrency(value: Double): String {
        val symbols = DecimalFormatSymbols(Locale("pt", "BR"))
        symbols.decimalSeparator = ','
        symbols.groupingSeparator = '.'

        val decimalFormat = DecimalFormat("R$ #,##0.00", symbols)

        return decimalFormat.format(value)
    }
}
