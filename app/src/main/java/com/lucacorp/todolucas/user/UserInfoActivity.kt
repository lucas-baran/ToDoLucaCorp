package com.lucacorp.todolucas.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.lucacorp.todolucas.R
import com.lucacorp.todolucas.databinding.ActivityFormBinding
import com.lucacorp.todolucas.databinding.ActivityUserInfoBinding

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userImage.load("https://cdn.discordapp.com/avatars/188385753686999040/164e4788ec23ffc3e58fe39cb3451694.png");
    }
}