package com.example.todoapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
import com.example.todoapp.network.NetworkListener
import com.example.todoapp.retrofit.todoApi
import kotlinx.coroutines.launch
import kotlin.math.log

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val todoListAdapter = TodoListAdapter()
   // private val networkListener: NetworkListener
    val viewModel: MainViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as TodoApplication).todoListRepositoryImpl)
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.countOfDoneFlow.flowWithLifecycle(
                viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                val str ="Выполнено - "+ viewModel.countOfDoneFlow.value.toString()
                //binding.tvDone.text = getString(R.string.todo_done , it ?: 0)
                Log.d("MainFragment", "str = " + str)
                binding.tvDone.text = str
            }
        }


       // checkNetworkAvailability(context: Context)



        return binding.root
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
                      viewModel.changeDoneState(todoListAdapter.currentList[position])


                }

                override fun onSwipedLeft(position: Int) {
                     viewModel.deleteTodoItem(checkForInternet(requireContext()),todoListAdapter.currentList[position],position)


                }
            })

        }

        todoListAdapter.onTodoItemClickListener = {
            findNavController().navigate(MainFragmentDirections.actionEditTodo(it.id))
        }
    }
    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}