package com.udacity.testing.basics.addtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.udacity.testing.basics.R
import com.udacity.testing.basics.util.Constant
import com.udacity.testing.basics.util.EventObserver
import com.udacity.testing.basics.util.setupRefreshLayout
import com.udacity.testing.basics.util.setupSnackbar

/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
class AddTaskFragment : Fragment() {

    private lateinit var viewDataBinding: AddtaskFragBinding

    private val args: AddEditTaskFragmentArgs by navArgs()

    private val addViewModel by viewModels<AddTaskViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_add_task, container, false)
        viewDataBinding = AddtaskFragBinding.bind(root).apply {
            this.viewModel = addViewModel
        }
        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSnackbar()
        setupNavigation()
        this.setupRefreshLayout(viewDataBinding.refreshLayout)
        addViewModel.start(args.taskId)
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, addViewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupNavigation() {
        addViewModel.taskUpdatedEvent.observe(viewLifecycleOwner, EventObserver {
            val action = AddEditTaskFragmentDirections
                .actionAddEditTaskFragmentToTasksFragment(Constant.ADD_EDIT_RESULT_OK)
            findNavController().navigate(action)
        })
    }
}