package com.lucacorp.todolucas.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lucacorp.todolucas.R
import com.lucacorp.todolucas.databinding.FragmentTaskListBinding
import com.lucacorp.todolucas.form.FormActivity
import java.util.*

class TaskListFragment : Fragment() {

    private lateinit var binding: FragmentTaskListBinding

    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = TaskListAdapter()
        binding.recyclerView.adapter = adapter
        adapter.submitList(taskList.toList())
        adapter.onClickDelete = { task ->
            taskList.remove(task)
            adapter.submitList(taskList.toList())
        }

        val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = result.data?.getSerializableExtra("task") as? Task
            if (task != null)
            {
                taskList.add(task)
                adapter.submitList(taskList.toList())
            }
        }

        binding.addTaskFloatingButton.setOnClickListener(){
            val intent = Intent(activity, FormActivity::class.java)
            formLauncher.launch(intent)
        }
    }
}