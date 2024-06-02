package com.senac.mintwallet.ui.home.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentNewTransferBinding
import com.senac.mintwallet.helpers.MoneyTextWatcher
import com.senac.mintwallet.model.Repository
import com.senac.mintwallet.model.TransferEntity
import com.senac.mintwallet.ui.component.toast.toast
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class NewTransfer: Fragment() {
    private val binding get() = _binding!!
    private var _binding: FragmentNewTransferBinding? = null

    private val args: NewTransferArgs by navArgs()
    private var transferEntity: TransferEntity? = TransferEntity()
    private var repository: Repository = Repository();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTransferBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.statusBarColor = resources.getColor(R.color.background_menu);

        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners() {
        binding.amount.addTextChangedListener(MoneyTextWatcher(binding.amount))
        binding.submitButton.setOnClickListener {this.newTransfer()}
        binding.backButton.setOnClickListener {findNavController().popBackStack()}
    }

    private fun newTransfer() {
        val regex = Regex("^\\d{1,3}(\\.\\d{3})*,\\d{2}$")
        val amount = binding.amount.text.toString()

        if (amount.isEmpty() || !regex.matches(amount)) {
            return toast.showToast(this.requireContext(), getString(R.string.message_amount_required), false)
        }

        transferEntity?.amount = onlyNumber(binding.amount.text.toString())
        transferEntity?.describe  = binding.describe.text.toString()
        transferEntity?.typeTransfer = args.typeTransfer

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        transferEntity?.createdAt = LocalDateTime.now().format(formatter)

        val transferCreated = this.repository.createNewTransfer(transferEntity!!)

        transferEntity!!.uuid = transferCreated.uuid
        val action = NewTransferDirections.actionNewTransferToRegisterNewTransfer(transferEntity!!)

        findNavController().navigate(action)
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