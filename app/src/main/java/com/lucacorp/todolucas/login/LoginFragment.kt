package com.lucacorp.todolucas.login

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings.Global.putString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lucacorp.todolucas.MainActivity
import com.lucacorp.todolucas.R
import com.lucacorp.todolucas.databinding.FragmentLoginBinding
import com.lucacorp.todolucas.form.FormActivity
import com.lucacorp.todolucas.network.Api
import com.lucacorp.todolucas.network.UserWebService
import com.lucacorp.todolucas.tasklist.Task
import com.lucacorp.todolucas.tasklist.TaskListFragment
import com.lucacorp.todolucas.user.UserInfoViewModel
import kotlinx.coroutines.launch

class LoginFragment: Fragment() {

    private val userViewModel = UserInfoViewModel()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            userViewModel.loginResponse.collect {
                if(it != null) {
                    PreferenceManager.getDefaultSharedPreferences(Api.appContext).edit {
                        putString(Api.SHARED_PREF_TOKEN_KEY, it?.token)
                    }

                    // Redirect
                    findNavController().navigate(R.id.action_loginFragment_to_taskListFragment)
                }
                else {
                    Toast.makeText(context, "Erreur de connexion", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.logInButton.setOnClickListener {
            if (binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()) {
                val loginForm = LoginForm(email = binding.emailEditText.text.toString(), password = binding.passwordEditText.text.toString())
                userViewModel.login(loginForm)
            }
            else{
                Toast.makeText(context, "Email ou mdp vide", Toast.LENGTH_LONG).show()
            }
        }
    }
}