package com.example.todoapp.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.ItemPriority
import com.example.todoapp.MainViewModel
import com.example.todoapp.R
import com.example.todoapp.TodoItem
import com.google.android.material.snackbar.Snackbar
import com.example.todoapp.databinding.FragmentTodoBinding
import java.util.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

class AddTodoFragment : Fragment() {

    private lateinit var binding: FragmentTodoBinding
    private lateinit var todoItem: TodoItem

    private var priorityMenu: PopupMenu? = null
    private val c = Calendar.getInstance()

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var _taskID: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = ComposeView(requireContext())
        view.apply {
            setContent {
               var mainText = remember {mutableStateOf("Что нужно сделать..")}
                val checkedState = remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier.padding(5.dp,30.dp ),
                ) {
                    Row() {

                        ImgClose()
                        Row(modifier = Modifier, horizontalArrangement = Arrangement.Center) {
                            SaveBtn(mainText)
                        }
                    }
                    MainCard(mainText)
                    Text(text = "Важность", style = TextStyle( fontSize = 16.sp,))
                    Text(text = "Нет")
                    Spacer(modifier = Modifier.height(30.dp))
                    Divider(color = Color.Black)
                    Spacer(modifier = Modifier.height(30.dp))
                    Row() {
                        Column(
                            modifier = Modifier,
                        ) {
                            Text(text = "Сделать до", style = TextStyle( fontSize = 16.sp,))
                            Text(text = "")
                        }
                    Checkbox(checked = checkedState.value, onCheckedChange = { checkedState.value = it })
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Divider(color = Color.Black)
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(modifier = Modifier, horizontalArrangement = Arrangement.SpaceBetween) {
                        ImgDelete()
                        Text(text="Удалить",color = Color.Red)
                    }
                }
            }
        }
        todoItem = TodoItem(
            id = c.timeInMillis.toString(),
            msg = "",
            priority = ItemPriority.NORMAL,
            deadline = null,
            isCompleted = false,
            createDate = Calendar.getInstance().time,
            changedDate = null
        )
        return view
        /* binding = FragmentTodoBinding.inflate(inflater, container, false)
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
         return binding.root*/
    }

    @Composable
    fun ImgClose() {
        Image(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "image",

            modifier = Modifier
                .padding(5.dp)
                .size(34.dp)

        )
    }
    @Composable
    fun ImgDelete() {
        Image(
            painter = painterResource(id = R.drawable.ic_delete),
            contentDescription = "image",

            modifier = Modifier
                .padding(5.dp)
                .size(34.dp)

        )

    }

    @Composable
    fun SaveBtn(mainText:MutableState<String>) {
        var text by remember { mainText }
        Button(onClick = {

            todoItem.msg = text
            Log.d("SaveBtn", "SaveBtn: "+todoItem.msg)
            val uuid = UUID.randomUUID()
            todoItem.id = uuid.toString()
            if (todoItem.msg.isNotBlank()) {

                viewModel.addTodoItem(todoItem)

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


        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent))
        {
            Text(modifier = Modifier, text = "Сохранить", color = Color.Red)
        }
    }

    @Composable
    fun MainCard(mainText:MutableState<String>) {
         var text by remember { mainText }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(100.dp),
            shape = RoundedCornerShape(15.dp),
            elevation = 5.dp
        ) {
            TextField(

                value = text,
                onValueChange = {
                    text = it
                },
            )


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
                val uuid = UUID.randomUUID()
                todoItem.id = uuid.toString()
                if (todoItem.msg.isNotBlank()) {

                    viewModel.addTodoItem(todoItem)

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

        // Хардкод
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