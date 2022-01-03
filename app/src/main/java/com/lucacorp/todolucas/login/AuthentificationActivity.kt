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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentification)
    }
}