package com.example.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import com.example.todoapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    private val todoListAdapter = TodoListAdapter()

    companion object{
        var countDone = MutableLiveData(0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

        binding = FragmentMainBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()) [MainViewModel::class.java]

        viewModel.todoList.observe(viewLifecycleOwner) { it ->
            var count = 0

            it.forEach {
                 if (it.isCompleted)
                     count += 1
            }

            countDone.value = count
            todoListAdapter.submitList(it)
        }

        countDone.observe(viewLifecycleOwner) {
            binding.tvDone.text = getString(R.string.todo_done, it ?: 0)
        }

        binding.fabAddTodo.setOnClickListener {
            findNavController().navigate(
                MainFragmentDirections.actionAddTodo()
            )
        }

        binding.cardAddNew.setOnClickListener {
            findNavController().navigate(
                MainFragmentDirections.actionAddTodo()
            )
        }

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView(){

        with(binding.rvMain){
            adapter = todoListAdapter
            layoutManager = LinearLayoutManager(requireContext())

            setListener(object :  SwipeLeftRightCallback.Listener {
                override fun onSwipedRight(position: Int) {
                    viewModel.changeDoneState(todoListAdapter.currentList[position])
                }

                override fun onSwipedLeft(position: Int) {
                    viewModel.deleteTodoItem(todoListAdapter.currentList[position])
                }
            })

        }

        todoListAdapter.onTodoItemClickListener = {
            findNavController().navigate(MainFragmentDirections.actionEditTodo(it.id))
        }
    }
}