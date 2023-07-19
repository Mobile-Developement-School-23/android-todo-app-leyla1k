package com.example.todoapp.ui.fragments
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.ItemPriority
import com.example.todoapp.R
import com.example.todoapp.TodoApplication
import com.example.todoapp.TodoItem
import com.example.todoapp.databinding.FragmentTodoBinding
import com.example.todoapp.di.TodoScope
import com.example.todoapp.ui.fragments.EditTodoFragmentArgs
import com.example.todoapp.ui.viewmodels.EditTodoViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

@TodoScope
class EditTodoFragment : Fragment() {
    private lateinit var binding: FragmentTodoBinding

    private val viewModel: EditTodoViewModel by viewModels {
        (requireActivity().application as TodoApplication).applicationComponent.viewModelFactory()
    }

    private val todoId by lazy { navArgs<EditTodoFragmentArgs>().value.todoId }
    private val c = Calendar.getInstance()
    private var priorityMenu: PopupMenu? = null
    private lateinit var todoItem: TodoItem

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as TodoApplication)
            .applicationComponent
            .editTodoItemComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoBinding.inflate(inflater, container, false)


        init()
        setupMenu()
        setupListeners()
        return binding.root
    }
    private fun init(){//переправить
        viewLifecycleOwner.lifecycleScope.launch {
            Log.d("Edit item id", todoId)
            todoItem = viewModel.getTodoItem(id = todoId)
            with(binding){
                tvMsg.setText(todoItem.msg)
                when(todoItem.priority){
                    ItemPriority.LOW -> {
                        binding.tvPriority.setTextColor(requireActivity().getColor(R.color.label_tertiary))
                        binding.tvPriority.text = "Низкий"
                    }
                    ItemPriority.URGENT -> {
                        binding.tvPriority.setTextColor(requireActivity().getColor(R.color.red))
                        binding.tvPriority.text = "!! Высокий"
                    }
                    else -> {
                        binding.tvPriority.setTextColor(requireActivity().getColor(R.color.label_tertiary))
                        binding.tvPriority.text = "Нет"
                    }
                }

                if (todoItem.deadline != null) {
                    switchDeadline.isChecked = true
                    tvDeadline.visibility = View.VISIBLE

                    setupDate(todoItem.deadline!!)
                }else{
                    switchDeadline.isChecked = false
                    tvDeadline.visibility = View.INVISIBLE
                    setupDate(null)
                }

                ivDelete
                    .setColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.red),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )

                tvDelete.setTextColor(requireActivity().getColor(R.color.red))
            }
        }
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
                if (switchDeadline.isChecked){
                    tvDeadline.visibility = View.VISIBLE
                    todoItem.deadline = Calendar.getInstance().time
                }else{
                    tvDeadline.visibility = View.INVISIBLE
                    todoItem.deadline = null
                }
            }

            tvDeadline.setOnClickListener {
                openDatePicker()
            }

            btnSave.setOnClickListener {
                todoItem.msg = tvMsg.text.toString()

                if (todoItem.msg.isNotBlank()) {
                    todoItem.changedDate = Calendar.getInstance().time


                    viewModel.editTodoItem(todoItem)


                    findNavController().popBackStack()
                } else {
                    val decorView = requireActivity().window.decorView
                    val view = decorView.findViewById(android.R.id.content) ?: decorView.rootView
                    Snackbar
                        .make(view, "Заполните все поля", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(requireActivity().getColor(R.color.back_secondary))
                        .show()

                }
            }


            llDelete.setOnClickListener {
                viewModel.deleteTodoItemWithoutPosition(todoItem)
                findNavController().popBackStack()
            }


        }

    }


    private fun setupMenu(){
        priorityMenu = PopupMenu(requireContext(), binding.tvPriority)

        priorityMenu!!.menuInflater.inflate(R.menu.menu_priority, priorityMenu!!.menu)

        priorityMenu!!.setOnMenuItemClickListener { item ->
            menuListener(item.itemId)
            return@setOnMenuItemClickListener true
        }
    }

    private fun menuListener(itemId: Int){
        when(itemId){
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