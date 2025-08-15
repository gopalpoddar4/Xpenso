package com.gopalpoddar4.xpenso.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gopalpoddar4.xpenso.R
import com.gopalpoddar4.xpenso.databinding.FragmentHomeBinding
import com.gopalpoddar4.xpenso.databinding.FragmentTransactionDetailsBinding
import com.gopalpoddar4.xpenso.viewmodel.MainViewModel


class TransactionDetailsFragment : Fragment() {

    private var _binding: FragmentTransactionDetailsBinding?=null
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
        _binding = FragmentTransactionDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: TransactionDetailsFragmentArgs by navArgs()
        val expenseId = args.ExpenseId

        binding.backDeatils.setOnClickListener {
            findNavController().navigateUp()
        }
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel.getExpenseById(expenseId,{
            binding.name.text = it.name
            binding.amount.text = "$ ${it.amount}"
            binding.date.text = it.date
            binding.note.text = it.note
        },{
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
        })

        binding.deleteExpenseBtn.setOnClickListener {
            mainViewModel.deleteExpense(expenseId,{
                Toast.makeText(requireContext(),"Expense deleted", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            })
        }


    }
}