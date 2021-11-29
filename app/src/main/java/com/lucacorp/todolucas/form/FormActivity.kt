package com.lucacorp.todolucas.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.lucacorp.todolucas.R
import com.lucacorp.todolucas.databinding.ActivityFormBinding
import com.lucacorp.todolucas.databinding.FragmentTaskListBinding
import com.lucacorp.todolucas.tasklist.Task
import java.util.*

class FormActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.validateTaskButton.setOnClickListener {
            val newTask = Task(
                id = UUID.randomUUID().toString(),
                title = binding.titleEditText.text.toString(),
                description = binding.descriptionEditText.text.toString()
            )
            intent.putExtra("task", newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}