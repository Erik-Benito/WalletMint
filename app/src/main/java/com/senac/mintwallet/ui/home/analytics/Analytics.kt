package com.senac.mintwallet.ui.home.analytics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.senac.mintwallet.databinding.FragmentAnalyticsBinding
import com.senac.mintwallet.model.ETypeTransfer
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Transfer(
    val amount: Double = 0.0,
    val createdAt: String = "",
    val describe: String = "",
    val typeTransfer: String = ""
)

class Analytics : Fragment() {
    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeAdapter: TransactionAdapter
    private val transfers: MutableList<Transfer> = mutableListOf()
    private lateinit var reference: DatabaseReference
    private var valueEventListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        valueEventListener?.let { reference.removeEventListener(it) }
        _binding = null
    }

    private fun initListeners() {

        recipeAdapter = TransactionAdapter(transfers)
        binding.recentTransactionsRecyclerView.adapter = recipeAdapter
        binding.recentTransactionsRecyclerView.layoutManager = LinearLayoutManager(context)

        val user = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase
            .getInstance()
            .getReference("users")
            .child(user!!.uid)
            .child("transfers")

        valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                transfers.clear()
                for (snapshot in dataSnapshot.children) {
                    val amount = snapshot.child("amount").getValue(Double::class.java) ?: 0.0
                    val createdAt = snapshot.child("createdAt").getValue(String::class.java) ?: ""
                    val describe = snapshot.child("describe").getValue(String::class.java) ?: ""
                    val typeTransfer = snapshot.child("typeTransfer").getValue(String::class.java) ?: ""

                    val transfer = Transfer(amount, createdAt, describe, typeTransfer)
                    transfers.add(transfer)
                }

                recipeAdapter.notifyDataSetChanged()
                calculatedAmountMonthly(transfers)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException())
            }
        }


        reference.orderByChild("createdAt").addValueEventListener(valueEventListener as ValueEventListener)
    }
    private fun convertStringToDateAndExtractMonth(dateString: String): Int {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val date = LocalDate.parse(dateString, formatter)
        return date.monthValue
    }

    private fun calculatedAmountMonthly(transfs: List<Transfer>){
        var total: Double = 0.0;
        var pay: Double = 0.0;
        var rec: Double = 0.0;

        transfs.forEach{transf ->
            if(convertStringToDateAndExtractMonth(transf.createdAt)  == LocalDate.now().monthValue) {

                if(transf.typeTransfer == ETypeTransfer.RECEIPT.toString())
                    rec += transf.amount

                if(transf.typeTransfer == ETypeTransfer.PAYMENT.toString())
                    pay += transf.amount
            }
        }

        total = rec - pay;

        binding.incomeAmount.text =formatCurrency(rec)
        binding.spendAmount.text = formatCurrency(pay)
        binding.transactionAmount.text = formatCurrency(total)
    }

    private fun formatCurrency(value: Double): String {
        val symbols = DecimalFormatSymbols(Locale("pt", "BR"))
        symbols.decimalSeparator = ','
        symbols.groupingSeparator = '.'

        val decimalFormat = DecimalFormat("R$ #,##0.00", symbols)

        return decimalFormat.format(value)
    }

}