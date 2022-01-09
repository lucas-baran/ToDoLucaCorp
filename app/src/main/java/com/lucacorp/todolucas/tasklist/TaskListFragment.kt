package com.lucacorp.todolucas.tasklist

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.lucacorp.todolucas.R
import com.lucacorp.todolucas.databinding.FragmentTaskListBinding
import com.lucacorp.todolucas.form.FormActivity
import com.lucacorp.todolucas.network.Api
import com.lucacorp.todolucas.user.UserInfoActivity
import com.lucacorp.todolucas.user.UserInfoViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {

    private lateinit var binding: FragmentTaskListBinding

    private val taskListViewModel: TaskListViewModel by viewModels()
    private val userViewModel: UserInfoViewModel by viewModels()

    private val adapterListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            /// Delete task in TasksRepository
            taskListViewModel.deleteTask(task)
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
            val oldTask = taskListViewModel.taskList.value.firstOrNull { it.id == task.id }
            if (oldTask != null) {
                taskListViewModel.updateTask(task)
            }
            else {
                taskListViewModel.createTask(task)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(inflater, container, false)

        val token = PreferenceManager.getDefaultSharedPreferences(Api.appContext).getString(Api.SHARED_PREF_TOKEN_KEY, "")
        // Redirection sir le token n'existe pas
        if(token == "") {
            findNavController().navigate(R.id.action_taskListFragment_to_authentificationFragment)
            return null;
        }

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
            taskListViewModel.taskList.collect {
                adapter.submitList(it)
            }
        }

        lifecycleScope.launch {
            userViewModel.userInfo.collect {
                binding.userInfoTextView.text = "${it?.firstName} ${it?.lastName}"

                binding.userImage.load(it?.avatar) {
                    // affiche une image en cas d'erreur:
                    error(R.drawable.ic_launcher_background)
                    transformations(CircleCropTransformation())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        taskListViewModel.refresh()
        userViewModel.refresh()
    }
}
