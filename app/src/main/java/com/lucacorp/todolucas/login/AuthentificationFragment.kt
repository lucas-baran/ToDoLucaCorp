package com.lucacorp.todolucas.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.lucacorp.todolucas.R
import com.lucacorp.todolucas.databinding.FragmentAuthentificationBinding

class AuthentificationFragment: Fragment() {

    private lateinit var binding: FragmentAuthentificationBinding

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthentificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        binding.logInAuthButton.setOnClickListener {
            navController.navigate(R.id.action_authentificationFragment_to_loginFragment)
        }

        binding.signUpAuthButton.setOnClickListener {
            navController.navigate(R.id.action_authentificationFragment_to_signupFragment)
        }
    }
}