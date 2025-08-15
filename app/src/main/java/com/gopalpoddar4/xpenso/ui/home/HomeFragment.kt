package com.gopalpoddar4.xpenso.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.gopalpoddar4.xpenso.R
import com.gopalpoddar4.xpenso.data.ExpenceModel
import com.gopalpoddar4.xpenso.data.TransationAdapter
import com.gopalpoddar4.xpenso.databinding.FragmentHomeBinding
import com.gopalpoddar4.xpenso.databinding.FragmentLoginBinding
import com.gopalpoddar4.xpenso.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class HomeFragment : Fragment() {

    private lateinit var fullName: String
    private var _binding: FragmentHomeBinding?=null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        activity?.window?.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.background)

        val today = LocalDate.now()
        val formmatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        val formateToday = today.format(formmatter)
        val sevenDaysAgo = today.minusDays(6)

        val formmatter1 = DateTimeFormatter.ofPattern("MMMM")
        val formateMonth = today.format(formmatter1)

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.primary)

        greating()

        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val db = FirebaseDatabase.getInstance().reference

        binding.transactionHistoryRcv.layoutManager = LinearLayoutManager(requireContext())

        uid.let {
            db.child("xpenso").child("users").child(it).get()
                .addOnSuccessListener { data ->
                    fullName = data.child("name").value.toString()
                    binding.name.text = fullName
                }
        }

        binding.addExpenceBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addExpenceFragment)
        }

        binding.profilePic.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.seeAllTranscation.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allTransationFragment)
        }

        binding.progressBarHome.visibility = View.VISIBLE

        mainViewModel.getAllExpense({expenseList->
            binding.progressBarHome.visibility = View.GONE
            val todayExpense = expenseList.filter { list-> list.date == formateToday }

            binding.transactionHistoryRcv.adapter = TransationAdapter(todayExpense,{
                val action = HomeFragmentDirections.actionHomeFragmentToTransactionDetailsFragment(it)
                findNavController().navigate(action)
            })

            val totalAmount = todayExpense.sumOf { it.amount?:0.0 }
            binding.todaysExpense.text = "$ $totalAmount"

            val monthlyExpense = expenseList.filter { it.month == formateMonth }
            val monthlyAmount = monthlyExpense.sumOf { it.amount?:0.0 }
            binding.thisMonthExpense.text = "$ $monthlyAmount"

            val last7daysData = expenseList.filter {
                try {
                    val itemDate = LocalDate.parse(it.date, DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH))
                    itemDate in sevenDaysAgo..today
                }catch (e: Exception){
                    false
                }
            }
            val last7daysAmount = last7daysData.sumOf { it.amount?:0.0 }
            binding.last7DaysExpense.text = "$ $last7daysAmount"


        },{onTaskFailed->
            Toast.makeText(requireContext(),onTaskFailed, Toast.LENGTH_SHORT).show()
        })


    }

    private fun greating(){
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greating = when(hour){
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            in 17..20 -> "Good evening"
            else -> "Good night"
        }
        binding.goodMorning.text = "$greating"
    }


}