package com.lucacorp.todolucas.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lucacorp.todolucas.databinding.FragmentAuthentificationBinding

class AuthentificationFragment: Fragment() {

    private lateinit var binding: FragmentAuthentificationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthentificationBinding.inflate(inflater, container, false)
        return binding.root
    }
}