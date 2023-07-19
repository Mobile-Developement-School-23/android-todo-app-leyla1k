package com.example.todoapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.PopupMenu

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.ui.viewmodels.MainViewModel
import com.example.todoapp.R
import com.example.todoapp.TodoApplication
import com.google.android.material.transition.MaterialSharedAxis
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import com.example.todoapp.databinding.FragmentMainBinding
import com.example.todoapp.network.InternetConnectionWatcher
import com.example.todoapp.network.TodoApi
import com.example.todoapp.shared.Constants.KEY_THEME
import com.example.todoapp.shared.Constants.PREFS_NAME
import com.example.todoapp.shared.Constants.THEME_DARK
import com.example.todoapp.shared.Constants.THEME_LIGHT
import com.example.todoapp.shared.Constants.THEME_UNDEFINED
import com.example.todoapp.ui.adapters.TodoListAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val todoListAdapter = TodoListAdapter()

    private val viewModel: MainViewModel by viewModels {
        (requireActivity().application as TodoApplication).applicationComponent.viewModelFactory()
    }

    @Inject
    lateinit var todoApi: TodoApi
    private var popupMenuSettings: PopupMenu? = null
    private val sharedPrefs by lazy {   activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as TodoApplication).applicationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        binding = FragmentMainBinding.inflate(inflater, container, false)
        initTheme()
        setupMenu()
        binding.btnSettings.setOnClickListener {
            popupMenuSettings?.show()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listOfNotesFlow.flowWithLifecycle(
                viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED
            ).collect { dataFromDB ->
                todoListAdapter.submit(dataFromDB)
            }
        }
        setupRecyclerView()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.countOfDoneFlow.flowWithLifecycle(
                viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                val str ="Выполнено - "+ viewModel.countOfDoneFlow.value.toString()
                Log.d("MainFragment", "str = " + str)
                binding.tvDone.text = str
            }
        }
        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError)
                onNetworkError()
        }
        return binding.root
    }
    private fun getSavedTheme() = sharedPrefs!!.getInt(KEY_THEME, THEME_UNDEFINED)
    private fun initTheme() {
        when (getSavedTheme()) {
            THEME_LIGHT ->  setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
            THEME_DARK -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
        }
    }
    private fun saveTheme(theme: Int) = sharedPrefs!!.edit().putInt(KEY_THEME, theme).apply()
    private fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveTheme(prefsMode)
    }
    private fun setupMenu() {
        popupMenuSettings = PopupMenu(requireContext(), binding.btnSettings)
        popupMenuSettings!!.menuInflater.inflate(R.menu.menu_settings, popupMenuSettings!!.menu)
        popupMenuSettings!!.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.undone -> {
                // перепукать адаптер
                    //todoListAdapter.showUndone()
                }
                R.id.dark_theme -> {
                    saveTheme(1)
                    setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
                    Log.d("THEMES", "setupMenu: dark")
                }
                R.id.light_theme -> {
                    saveTheme(0)
                    setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
                    Log.d("THEMES", "setupMenu: light")
                }
                else -> {
                }
            }
            return@setOnMenuItemClickListener true
        }
    }
    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            val rootView = view

            rootView?.let {
                Snackbar.make(it, getString(R.string.network_error_message), Snackbar.LENGTH_LONG)
                    .show()
            }
            viewModel.onNetworkErrorShown()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val internetConnectionWatcher = InternetConnectionWatcher(requireContext())
        internetConnectionWatcher.setOnInternetConnectedListener(viewModel::refreshDataFromRepository)
        internetConnectionWatcher.startWatching()
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
                    viewModel.deleteTodoItem(todoListAdapter.currentList[position],position)
                }

            }

            )
        }

        todoListAdapter.onTodoItemClickListener = {
            findNavController().navigate(
                MainFragmentDirections.actionEditTodo(
                    it.id
                )
            )
        }
        todoListAdapter.onCheckBoxClickListener = {
              viewModel.changeDoneState(it)

        }
    }



}