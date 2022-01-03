package com.lucacorp.todolucas.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.lucacorp.todolucas.R
import com.lucacorp.todolucas.databinding.ActivityUserInfoBinding
import com.lucacorp.todolucas.databinding.FragmentAuthentificationBinding

class AuthentificationActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentification)

        navController = findNavController(R.id.nav_host_fragment)

        findViewById<Button>(R.id.logInAuthButton).setOnClickListener {
            navController.navigate(R.id.action_authentificationFragment_to_loginFragment)
        }

        findViewById<Button>(R.id.signUpAuthButton).setOnClickListener {
            navController.navigate(R.id.action_authentificationFragment_to_signupFragment)
        }
    }
}