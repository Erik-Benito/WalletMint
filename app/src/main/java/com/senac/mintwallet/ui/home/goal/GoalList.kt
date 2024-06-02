package com.senac.mintwallet.ui.home.goal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentGoalListBinding

class GoalList: Fragment() {
    private var _binding: FragmentGoalListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SavingPlanAdapter
    private lateinit var reference: DatabaseReference

    private var valueEventListener: ValueEventListener? = null
    private val goals: MutableList<SavingPlan> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListens()
    }

    private fun initListens() {
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

        binding.submitButton.setOnClickListener {
            findNavController().navigate(R.id.choice_goal)
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        valueEventListener?.let { reference.removeEventListener(it) }
        _binding = null
    }
}

