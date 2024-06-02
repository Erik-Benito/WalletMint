package com.senac.mintwallet.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentHomeMenuBinding
import com.senac.mintwallet.model.ETypeTransfer
import com.senac.mintwallet.ui.home.analytics.TransactionAdapter
import com.senac.mintwallet.ui.home.analytics.Transfer
import com.senac.mintwallet.ui.home.goal.SavingPlan
import com.senac.mintwallet.ui.home.goal.SavingPlanAdapter
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeMenu: Fragment() {
    private var _binding: FragmentHomeMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeAdapterTransfer: TransactionAdapter
    private val transfers: MutableList<Transfer> = mutableListOf()
    private lateinit var referenceTransfer: DatabaseReference
    private var valueEventListenerTransfer: ValueEventListener? = null


    private lateinit var adapter: SavingPlanAdapter
    private lateinit var reference: DatabaseReference
    private var valueEventListener: ValueEventListener? = null
    private val goals: MutableList<SavingPlan> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeMenuBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateToNextScreen()
    }

    private fun navigateToNextScreen() {
        TransferInit()
        initGoal()
    }

    private fun initGoal() {
        binding.recyclerView.layoutManager = GridLayoutManager(this.requireContext(), 2)

        adapter = SavingPlanAdapter(this.requireContext(), goals) {}
        binding.recyclerView.adapter = adapter

        valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                goals.clear()
                for (snapshot in dataSnapshot.children) {
                    val amount = snapshot.child("amountTotal").getValue(Double::class.java) ?: 0.0
                    val amountMonthly = snapshot.child("amountMonthly").getValue(Double::class.java) ?: 0.0
                    val dayIndicator = snapshot.child("dayIndicator").getValue(String::class.java) ?: ""
                    val type = snapshot.child("type").getValue(String::class.java) ?: ""
                    val uuid = snapshot.child("uuid").getValue(String::class.java) ?: ""

                    val goal = SavingPlan(type, amount, searchForImage(type))
                    goals.add(goal)
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException())
            }
        }

        val user = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase
            .getInstance()
            .getReference("users")
            .child(user!!.uid)
            .child("goals")
        reference.addValueEventListener(valueEventListener as ValueEventListener)

    }

    private fun searchForImage(type: String): Int {
        return when(type) {
            "Casa" -> R.drawable.home
            "Viagem" -> R.drawable.vacations
            "Carro", -> R.drawable.car
            "Casamento", -> R.drawable.wedding
            else -> 0
        }
    }


    private fun TransferInit() {
        recipeAdapterTransfer = TransactionAdapter(transfers)
        binding.recyclerTransctions.adapter = recipeAdapterTransfer
        binding.recyclerTransctions.layoutManager = LinearLayoutManager(context)

        val user = FirebaseAuth.getInstance().currentUser
        referenceTransfer = FirebaseDatabase
            .getInstance()
            .getReference("users")
            .child(user!!.uid)
            .child("transfers")

        valueEventListenerTransfer = object : ValueEventListener {
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

                recipeAdapterTransfer.notifyDataSetChanged()
                calculatedAmountMonthly(transfers)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException())
            }
        }


        referenceTransfer.orderByChild("createdAt").addValueEventListener(valueEventListenerTransfer as ValueEventListener)
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

        binding.value.text = formatCurrency(pay)
        binding.wallet.text = formatCurrency(total)
        binding.percentage.text = String.format("%.2f", (pay * 100) / total) + "%"
    }

    private fun formatCurrency(value: Double): String {
        val symbols = DecimalFormatSymbols(Locale("pt", "BR"))
        symbols.decimalSeparator = ','
        symbols.groupingSeparator = '.'

        val decimalFormat = DecimalFormat("R$ #,##0.00", symbols)

        return decimalFormat.format(value)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        valueEventListenerTransfer?.let { referenceTransfer.removeEventListener(it) }
        _binding = null
    }
}