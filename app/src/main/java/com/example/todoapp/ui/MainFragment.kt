package com.example.todoapp.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.PopupMenu

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.MainViewModel
import com.example.todoapp.R
import com.example.todoapp.TodoApplication
import com.example.todoapp.TodoListAdapter
import com.google.android.material.transition.MaterialSharedAxis
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import com.example.todoapp.databinding.FragmentMainBinding
import com.example.todoapp.localbase.ViewModelFactory
import com.example.todoapp.network.InternetConnectionWatcher
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

const val PREFS_NAME = "theme_prefs"
const val KEY_THEME = "prefs.theme"
const val THEME_UNDEFINED = -1
const val THEME_LIGHT = 0
const val THEME_DARK = 1

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val todoListAdapter = TodoListAdapter()
    val viewModel: MainViewModel by activityViewModels {
        ViewModelFactory((requireActivity().application as TodoApplication).todoListRepositoryImpl, requireActivity().application)
    }
    private var popupMenuSettings: PopupMenu? = null
    private val sharedPrefs by lazy {   activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }


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

       /* binding.fabAddTodo.setOnClickListener {
            findNavController().navigate(
                MainFragmentDirections.actionAddTodo()
            )
        }

        binding.cardAddNew.setOnClickListener {
            findNavController().navigate(
                com.example.todoapp.ui.MainFragmentDirections.actionAddTodo()
            )
        }*/

        binding.btnSettings.setOnClickListener {
            popupMenuSettings?.show()
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

            /*THEME_UNDEFINED -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                   *//* Configuration.UI_MODE_NIGHT_NO -> themeLight.isChecked = true
                    Configuration.UI_MODE_NIGHT_YES -> themeDark.isChecked = true
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> themeLight.isChecked = true*//*
                }
            }*/
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

        //  надо убрать наверное
        popupMenuSettings!!.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.undone -> {

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
            })

        }

        todoListAdapter.onTodoItemClickListener = {
            findNavController().navigate(
               MainFragmentDirections.actionEditTodo(
                    it.id
                )
            )
        }
    }

}