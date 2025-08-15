package com.gopalpoddar4.xpenso.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gopalpoddar4.xpenso.R
import com.gopalpoddar4.xpenso.databinding.FragmentAddExpenceBinding
import com.gopalpoddar4.xpenso.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class AddExpenceFragment : Fragment() {

    private var _binding: FragmentAddExpenceBinding?=null
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
        _binding = FragmentAddExpenceBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val today = LocalDate.now()
        val formmatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        val formateToday = today.format(formmatter)

        binding.expenceDate.setText(formateToday)

        binding.backAddExpence.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.addExpenceBtn.setOnClickListener {
            addExpense()
        }
    }
    private fun addExpense(){
        val name = binding.expenceName.text.toString()
        val amount = binding.expenceAmount.text.toString()
        val date = binding.expenceDate.text.toString()
        val description = binding.expenceDesc.text.toString()

        if (name.isNotEmpty() && amount.isNotEmpty() && date.isNotEmpty()){
            val amount1  = amount.toDouble()

            mainViewModel.addExpense(name,date,amount1,description,{
                Toast.makeText(context,"Expense Added", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()

            },{
                Toast.makeText(requireContext(),it, Toast.LENGTH_SHORT).show()
            })
        }
    }
}