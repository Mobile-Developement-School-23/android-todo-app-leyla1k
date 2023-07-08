package com.example.todoapp

import android.app.DatePickerDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.example.todoapp.databinding.FragmentTodoBinding
import com.example.todoapp.network.NetworkListener
import com.example.todoapp.network.getRevision
import com.example.todoapp.network.setRevision
import java.util.*

class AddTodoFragment : Fragment() {

    private lateinit var binding: FragmentTodoBinding
    private lateinit var todoItem: TodoItem
    //private lateinit var todoListRepository:
    private var priorityMenu: PopupMenu? = null
    private val c = Calendar.getInstance()
    //private val network:NetworkListener
    private val viewModel: MainViewModel by activityViewModels ()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

        binding = FragmentTodoBinding.inflate(inflater, container, false)




        with(binding) {

            todoItem = TodoItem(
                id = c.timeInMillis.toString(),
                msg = "",
                priority = ItemPriority.NORMAL,
                deadline = null,
                isCompleted = false,
                createDate = Calendar.getInstance().time,
                changedDate = null
            )


            setupMenu()

            setupDate(c.time)
            ivDelete
                .setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.label_disable),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )

            tvDelete.setTextColor(requireActivity().getColor(R.color.label_disable))

        }


        setupListeners()


        return binding.root
    }


    private fun setupListeners() {
        with(binding) {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }

            tvPriority.setOnClickListener {
                priorityMenu?.show()
            }

            switchDeadline.setOnClickListener {
                if (switchDeadline.isChecked) {
                    tvDeadline.visibility = View.VISIBLE
                    todoItem.deadline = Calendar.getInstance().time
                } else {
                    tvDeadline.visibility = View.INVISIBLE
                    todoItem.deadline = null
                }
            }

            tvDeadline.setOnClickListener {
                openDatePicker()
            }

            btnSave.setOnClickListener {
                todoItem.msg = tvMsg.text.toString()
                val uuid= UUID.randomUUID()
                todoItem.id = uuid.toString()
                if (todoItem.msg.isNotBlank()) {
                    viewModel.addTodoItem(checkForInternet(requireContext()),requireContext().getRevision(),todoItem)
                    requireContext().setRevision(requireContext().getRevision()+1);
                    Log.d("AddTodoFragment", "revision: "+ requireContext().getRevision().toString())
                    findNavController().popBackStack()
                } else {
                    val decorView = requireActivity().window.decorView
                    val view = decorView.findViewById(android.R.id.content) ?: decorView.rootView
                    Snackbar
                        .make(view, "Заполните все поля", Snackbar.LENGTH_LONG)
                        .setTextColor(requireActivity().getColor(R.color.label_primary))
                        .setBackgroundTint(requireActivity().getColor(R.color.back_secondary))
                        .show()
                }
            }
        }
    }

    private fun openDatePicker() {
        if (todoItem.deadline != null) {
            val c = Calendar.getInstance()
            c.time = todoItem.deadline!!
        }

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, y, m, d ->
                val calendar = Calendar.getInstance()
                calendar.set(y, m, d)
                setupDate(calendar.time)
                todoItem.deadline = calendar.time
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun setupMenu() {
        priorityMenu = PopupMenu(requireContext(), binding.tvPriority)

        priorityMenu!!.menuInflater.inflate(R.menu.menu_priority, priorityMenu!!.menu)

        // Хардкодить плохо, но я разбойник
        priorityMenu!!.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.low -> {
                    binding.tvPriority.setTextColor(requireActivity().getColor(R.color.label_tertiary))
                    binding.tvPriority.text = "Низкий"
                    todoItem.priority = ItemPriority.LOW
                }
                R.id.normal -> {
                    binding.tvPriority.setTextColor(requireActivity().getColor(R.color.label_tertiary))
                    binding.tvPriority.text = "Нет"
                    todoItem.priority = ItemPriority.NORMAL
                }
                else -> {
                    binding.tvPriority.setTextColor(requireActivity().getColor(R.color.red))
                    binding.tvPriority.text = "!! Высокий"
                    todoItem.priority = ItemPriority.URGENT
                }
            }
            return@setOnMenuItemClickListener true
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
    private fun setupDate(date: Date?) {
        val c = Calendar.getInstance()
        if (date != null)
            c.time = date

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.tvDeadline.text = getString(R.string.date, day, month, year)
    }
}