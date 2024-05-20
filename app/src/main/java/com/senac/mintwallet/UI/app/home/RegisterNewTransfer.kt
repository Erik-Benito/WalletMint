package com.senac.mintwallet.UI.app.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentRegisterNewTransferBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.format.DateTimeFormatter
import java.util.Locale

class RegisterNewTransfer: Fragment() {
    private var _binding: FragmentRegisterNewTransferBinding? = null
    private val binding get() = _binding!!

    private val args: RegisterNewTransferArgs by navArgs()
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterNewTransferBinding.inflate(inflater,container,false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.home_graph)
            }
        })
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
        binding.submitButton.setOnClickListener {findNavController().navigate(R.id.home_graph)}

        binding.amountText.text = formatCurrency(args.transfer.amount!!)
        binding.idTransaction.text = args.transfer.uuid
        binding.transferCreated.text = args.transfer.createdAt?.format(formatter)
    }


    private fun formatCurrency(value: Double): String {
        val symbols = DecimalFormatSymbols(Locale("pt", "BR"))
        symbols.decimalSeparator = ','
        symbols.groupingSeparator = '.'

        val decimalFormat = DecimalFormat("R$ #,##0.00", symbols)

        return decimalFormat.format(value)
    }
}