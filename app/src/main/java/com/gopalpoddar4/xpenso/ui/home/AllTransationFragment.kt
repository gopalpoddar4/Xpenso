package com.gopalpoddar4.xpenso.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gopalpoddar4.xpenso.R
import com.gopalpoddar4.xpenso.data.TransationAdapter
import com.gopalpoddar4.xpenso.databinding.FragmentAllTransationBinding
import com.gopalpoddar4.xpenso.databinding.FragmentTransactionDetailsBinding
import com.gopalpoddar4.xpenso.viewmodel.MainViewModel


class AllTransationFragment : Fragment() {

    private var _binding: FragmentAllTransationBinding?=null
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
        _binding = FragmentAllTransationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.backAll.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.allTransationRcv.layoutManager = LinearLayoutManager(requireContext())

        mainViewModel.getAllExpense({expenseList->
            binding.allTransationRcv.adapter = TransationAdapter(expenseList,{
                val action = AllTransationFragmentDirections.actionAllTransationFragmentToTransactionDetailsFragment(it)
                findNavController().navigate(action)
            })
        },{

        })

    }


}