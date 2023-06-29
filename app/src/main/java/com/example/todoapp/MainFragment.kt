package com.example.todoapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import com.example.todoapp.databinding.FragmentMainBinding
import com.example.todoapp.localbase.TodoItemDao
import com.example.todoapp.localbase.MainDb
import com.example.todoapp.localbase.ViewModelFactory
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding



    private val todoListAdapter = TodoListAdapter()

    val viewModel: MainViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as TodoApplication).todoListRepositoryImpl)

    }

    companion object {
        var countDone = MutableLiveData(0)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("Attach", "onAttach: ")

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("createee", "onCreate: ")

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

        binding = FragmentMainBinding.inflate(inflater, container, false)

        Log.d("StartsAgain", "onCreateView: ")





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


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listOfNotesFlow.flowWithLifecycle(
                viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED
            ).collect { dataFromDB ->
                todoListAdapter.submit(dataFromDB)
                Log.d("DEBAG4", "data" + dataFromDB.size)
            }
        }
        setupRecyclerView()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("destroy", "destroy: ")

    }

    private fun setupRecyclerView() {

        with(binding.rvMain) {
            adapter = todoListAdapter
            layoutManager = LinearLayoutManager(requireContext())

            setListener(object : SwipeLeftRightCallback.Listener {
                override fun onSwipedRight(position: Int) {
                    //  viewModel.changeDoneState(todoListAdapter.currentList[position])
                }

                override fun onSwipedLeft(position: Int) {
                    // viewModel.deleteTodoItem(todoListAdapter.currentList[position])
                }
            })

        }

        todoListAdapter.onTodoItemClickListener = {
            findNavController().navigate(MainFragmentDirections.actionEditTodo(it.id))
        }
    }
}