package com.gopalpoddar4.xpenso.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.gopalpoddar4.xpenso.R
import com.gopalpoddar4.xpenso.databinding.FragmentHomeBinding
import com.gopalpoddar4.xpenso.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding?=null
    private val binding get() = _binding!!

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

        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val db = FirebaseDatabase.getInstance().reference

        uid.let {
            db.child("xpenso").child("users").child(it).get()
                .addOnSuccessListener { data ->
                    binding.userName.text = data.child("name").value.toString()
                    binding.userEmail.text = data.child("email").value.toString()

                }
        }

        binding.backProfile.setOnClickListener {
            findNavController().navigateUp()
        }

    }


}