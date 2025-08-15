package com.gopalpoddar4.xpenso.ui.auth

import android.os.Bundle
import android.util.Patterns
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
import com.gopalpoddar4.xpenso.databinding.FragmentRegisterBinding
import com.gopalpoddar4.xpenso.viewmodel.AuthViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding?=null
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
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.accent)

        binding.alreadyHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.signupBtn.setOnClickListener {
            binding.progressBarLayoutSignup.visibility = View.VISIBLE
            val name = binding.nameSignupET.text.toString().trim()
            val email = binding.emailSignupET.text.toString().trim()
            val password = binding.passwordSignupET.text.toString().trim()
            val confirmPassword = binding.confirmPasswordSignupET.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                binding.progressBarLayoutSignup.visibility = View.GONE
                Toast.makeText(requireContext(),"Enter details", Toast.LENGTH_SHORT).show()
            }else if(password!=confirmPassword){
                binding.progressBarLayoutSignup.visibility = View.GONE
                Toast.makeText(requireContext(),"Password must be same as confirm password", Toast.LENGTH_SHORT).show()
            }else if (password.length<6){
                binding.progressBarLayoutSignup.visibility = View.GONE
                Toast.makeText(requireContext(),"Password must be 6 digit", Toast.LENGTH_SHORT).show()
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.progressBarLayoutSignup.visibility = View.GONE
                Toast.makeText(requireContext(),"Enter correct email", Toast.LENGTH_SHORT).show()
            }else{
                authViewModel.createAccountUsingEmailPass(name,email,password,{
                    binding.progressBarLayoutSignup.visibility = View.GONE
                    findNavController().navigate(R.id.action_registerFragment_to_homeFragment,null,
                        NavOptions.Builder().setPopUpTo(R.id.registerFragment,true).build())
                },{
                    binding.progressBarLayoutSignup.visibility = View.GONE
                    Toast.makeText(requireContext(),it, Toast.LENGTH_SHORT).show()
                })
            }
        }


    }


}