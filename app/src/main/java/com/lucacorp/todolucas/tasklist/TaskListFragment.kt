package com.lucacorp.todolucas.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lucacorp.todolucas.R
import com.lucacorp.todolucas.databinding.FragmentTaskListBinding
import com.lucacorp.todolucas.form.FormActivity
import com.lucacorp.todolucas.network.Api
import com.lucacorp.todolucas.network.TasksRepository
import com.lucacorp.todolucas.user.UserInfoActivity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class TaskListFragment : Fragment() {

    private lateinit var binding: FragmentTaskListBinding
    private val userWebService = Api.userWebService

    private val viewModel: TaskListViewModel by viewModels()

    private val adapterListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            /// Delete task in TasksRepository
            viewModel.deleteTask(task)
        }

        override fun onClickEdit(task: Task) {
            val intent = Intent(activity, FormActivity::class.java)
            intent.putExtra("task", task)
            formLauncher.launch(intent)
        }

        override fun onClickShare(task: Task) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, task.description)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private val adapter : TaskListAdapter = TaskListAdapter(listener= adapterListener)

    private val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task
        if (task != null)
        {
            val oldTask = viewModel.taskList.value.firstOrNull { it.id == task.id }
            if (oldTask != null) {
                viewModel.updateTask(task)
            }
            else {
                viewModel.createTask(task)
            }
        }
    }

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
        binding.recyclerView.adapter = adapter

        binding.addTaskFloatingButton.setOnClickListener {
            val intent = Intent(activity, FormActivity::class.java)
            formLauncher.launch(intent)
        }

        binding.userImage.setOnClickListener {
            val intent = Intent(activity, UserInfoActivity::class.java)
            formLauncher.launch(intent)
        }

        // on lance une coroutine car `collect` est `suspend`
        lifecycleScope.launch {
            viewModel.taskList.collectLatest {
                adapter.submitList(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()!!
            binding.userInfoTextView.text = "${userInfo.firstName} ${userInfo.lastName}"
        }

        lifecycleScope.launch {
            val result = userWebService.getInfo()
            if (result.isSuccessful){
                binding.userImage.load(result.body()?.avatar) {
                    transformations(CircleCropTransformation())
                }
            }
            else {
                binding.userImage.load(R.drawable.ic_launcher_background) {
                    transformations(CircleCropTransformation())
                }
            }
        }

        viewModel.refresh()
    }
}
