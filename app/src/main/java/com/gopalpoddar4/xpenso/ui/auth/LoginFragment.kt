package com.gopalpoddar4.xpenso.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.gopalpoddar4.xpenso.R
import com.gopalpoddar4.xpenso.databinding.FragmentLoginBinding
import com.gopalpoddar4.xpenso.viewmodel.AuthViewModel


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding?=null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        activity?.window?.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.background)

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.accent)

        //Redirect to Sign-Up screen
        binding.dontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        //Login with Email & Password
        binding.loginBtn.setOnClickListener {
            binding.progressBarLayoutLogin.visibility = View.VISIBLE

            val email = binding.emailLoginET.text.toString().trim()
            val password = binding.passwordLoginET.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()){
                binding.progressBarLayoutLogin.visibility = View.GONE
                Toast.makeText(requireContext(),"Enter details", Toast.LENGTH_SHORT).show()
            }else{

                authViewModel.loginAccountUsingEmailPass(email,password,{
                    binding.progressBarLayoutLogin.visibility = View.GONE
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment,null,
                        NavOptions.Builder().setPopUpTo(R.id.loginFragment,true).build())
                },{
                    binding.progressBarLayoutLogin.visibility = View.GONE
                    Toast.makeText(requireContext(),"Login Failed $it", Toast.LENGTH_SHORT).show()
                })
            }
        }
    }
}