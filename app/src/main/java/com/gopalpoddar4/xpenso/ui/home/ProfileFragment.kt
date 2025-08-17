package com.gopalpoddar4.xpenso.ui.home

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.gopalpoddar4.xpenso.R
import com.gopalpoddar4.xpenso.databinding.FragmentHomeBinding
import com.gopalpoddar4.xpenso.databinding.FragmentProfileBinding
import com.gopalpoddar4.xpenso.viewmodel.MainViewModel


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding?=null
    private val binding get() = _binding!!
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sharedPref = requireActivity().getSharedPreferences("data", MODE_PRIVATE)

        binding.userName.text = sharedPref.getString("name","user")
        binding.userEmail.text = sharedPref.getString("email","Email")

        binding.backProfile.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.logOut.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment,null,NavOptions.Builder().setPopUpTo(R.id.profileFragment,true).build())

        }

    }


}