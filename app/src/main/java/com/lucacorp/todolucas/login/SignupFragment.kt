package com.lucacorp.todolucas.login

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lucacorp.todolucas.R
import com.lucacorp.todolucas.databinding.FragmentSignupBinding
import com.lucacorp.todolucas.network.Api
import kotlinx.coroutines.launch

class SignupFragment: Fragment() {

    private val userWebService = Api.userWebService
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpButton.setOnClickListener {
            if (binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
                && binding.firstnameEditText.text.isNotEmpty() && binding.lastnameEditText.text.isNotEmpty()
                && binding.confirmPasswordEditText.text.isNotEmpty()
                && binding.passwordEditText.text.toString() == binding.confirmPasswordEditText.text.toString()) {
                lifecycleScope.launch {
                    val signupForm = SignUpForm(
                        firstname = binding.firstnameEditText.text.toString(),
                        lastname = binding.lastnameEditText.text.toString(),
                        email = binding.emailEditText.text.toString(),
                        password = binding.passwordEditText.text.toString(),
                        password_confirmation = binding.confirmPasswordEditText.text.toString()
                    )
                    val response = userWebService.signup(signupForm)

                    if (response.isSuccessful){
                        PreferenceManager.getDefaultSharedPreferences(context).edit {
                            putString(Api.SHARED_PREF_TOKEN_KEY, response.body()?.token)
                        }
                        findNavController().navigate(R.id.action_signupFragment_to_taskListFragment)
                    }
                    else{
                        Toast.makeText(context, "Erreur de création", Toast.LENGTH_LONG).show()
                    }
                }
            }
            else{
                Toast.makeText(context, "Tous les champs sont requis!", Toast.LENGTH_LONG).show()
            }
        }
    }
}