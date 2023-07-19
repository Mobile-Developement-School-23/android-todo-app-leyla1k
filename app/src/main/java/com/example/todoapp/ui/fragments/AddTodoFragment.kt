package com.example.todoapp.ui.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.PopupMenu
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.ItemPriority
import com.example.todoapp.R
import com.example.todoapp.TodoItem
import com.google.android.material.snackbar.Snackbar
import java.util.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import com.example.todoapp.ui.viewmodels.AddTodoViewModel
import com.example.todoapp.TodoApplication
import com.example.todoapp.di.TodoScope

@TodoScope
class AddTodoFragment : Fragment() {

    //  private lateinit var binding: FragmentTodoBinding
    private lateinit var todoItem: TodoItem

    private var priorityMenu: PopupMenu? = null
    private val c = Calendar.getInstance()
    private val viewModel: AddTodoViewModel by viewModels {
        (requireActivity().application as TodoApplication).applicationComponent.viewModelFactory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as TodoApplication)
            .applicationComponent
            .addTodoItemComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())
        view.apply {
            setContent {
                val mainText = remember { mutableStateOf("") }
                val checkedState = remember { mutableStateOf(false) }
                val dateState = remember { mutableStateOf("") }

                val mYear: Int
                val mMonth: Int
                val mDay: Int
                val now = Calendar.getInstance()
                mYear = now.get(Calendar.YEAR)
                mMonth = now.get(Calendar.MONTH)
                mDay = now.get(Calendar.DAY_OF_MONTH)
                now.time = Date()
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                        val cal = Calendar.getInstance()
                        cal.set(year, month, dayOfMonth)
                        dateState.value =
                            "$year.$month.$dayOfMonth"
                        todoItem.deadline = cal.time
                    }, mYear, mMonth, mDay
                )


                Column(
                    modifier = Modifier.padding(5.dp, 30.dp),
                ) {


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                        ) {
                            ImgClose()
                        }
                        SaveBtn(mainText)
                    }


                    MainCard(mainText)
                    Text(text = "Важность", style = TextStyle(fontSize = 16.sp))
                    PriorityMenu()
                    Spacer(modifier = Modifier.height(30.dp))
                    Divider(color = Color.Black)
                    Spacer(modifier = Modifier.height(30.dp))


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(text = "Сделать до", style = TextStyle(fontSize = 16.sp))
                            Text(text = dateState.value,modifier = Modifier.clickable{datePickerDialog.show()})  //openDatePicker() по нажатию,
                        }



                        Switch(checked = checkedState.value,
                            onCheckedChange = {
                                checkedState.value = it; if (!it) {
                                todoItem.deadline = null
                                dateState.value=""
                            } else {
                                datePickerDialog.show()

                            }
                            })
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                    Divider(color = Color.Black)
                    Spacer(modifier = Modifier.height(30.dp))

                    Row(modifier = Modifier, horizontalArrangement = Arrangement.SpaceBetween) {
                        ImgDelete()
                        Text(text = "Удалить", color = Color.Red,modifier = Modifier.clickable { findNavController().popBackStack()})
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


    fun datePick(){

    }





    @Composable
    fun ChooseData(context: Context,dateState: MutableState<String>) {
        val mYear: Int
        val mMonth: Int
        val mDay: Int
        val now = Calendar.getInstance()
        mYear = now.get(Calendar.YEAR)
        mMonth = now.get(Calendar.MONTH)
        mDay = now.get(Calendar.DAY_OF_MONTH)
        now.time = Date()
        var date by remember { dateState }

        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val cal = Calendar.getInstance()
                cal.set(year, month, dayOfMonth)
                 //date = getFormattedDate(cal.time, "dd MMM,yyy")
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }
    @Composable
    fun PriorityMenu() {
        var expanded by remember { mutableStateOf(false) }

        val priorityState = remember { mutableStateOf("Нет") }
        var priorityStr by remember { priorityState }

        val priorityStateStyleText =
            remember { mutableStateOf(Color(red = 0x0, green = 0x0, blue = 0x0, alpha = 0x30)) }
        var priorityStrStyleText by remember { priorityStateStyleText }

        val color1 = Color(red = 0x0, green = 0x0, blue = 0x0, alpha = 0x30)

        /*val textStyle = TextStyle(
            color = Color.LightGray,
            fontSize = 14.sp,
        )*/

        Button(onClick = {
            expanded = true
            Log.d("PriorityMenu", "PriorityMenu: ")
            priorityMenu?.show()
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)) {
            Text(modifier = Modifier, text = priorityStr, color = priorityStrStyleText)

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Низкий") },
                    onClick = {
                        priorityStrStyleText = color1
                        priorityStr = "Низкий"
                        todoItem.priority = ItemPriority.LOW
                        expanded = false
                    }
                )
                Divider()
                DropdownMenuItem(
                    text = { Text("Нет") },
                    onClick = {
                        priorityStrStyleText = color1
                        priorityStr = "Нет"
                        todoItem.priority = ItemPriority.NORMAL
                        expanded = false
                    }
                )
                Divider()
                DropdownMenuItem(
                    text = { Text("!! Высокий") },
                    onClick = {
                        priorityStrStyleText = Color.Red
                        priorityStr = "!! Высокий"
                        todoItem.priority = ItemPriority.URGENT
                        expanded = false
                    }
                )
            }

        }

    }

    @Composable
    fun ImgClose() {
        Image(
            painter = painterResource(id = R.drawable.ic_close), contentDescription = "image",
            modifier = Modifier
                .padding(5.dp)
                .size(34.dp)
                .clickable { findNavController().popBackStack() }

        )
    }

    @Composable
    fun ImgDelete() {
        val colorMatrix = floatArrayOf(
            1f, 0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
        Image(
            painter = painterResource(id = R.drawable.ic_delete), contentDescription = "image", colorFilter= ColorFilter.colorMatrix(
                ColorMatrix(colorMatrix)
            ),
            modifier = Modifier
                .padding(5.dp)
                .size(34.dp).clickable { findNavController().popBackStack()}
        )
    }

    @Composable
    fun SaveBtn(mainText: MutableState<String>) {
        var text by remember { mainText }
        Button(onClick = {
            Log.d("SaveBtn", "SaveBtn: " + todoItem.msg)
            val uuid = UUID.randomUUID()
            todoItem.id = uuid.toString()
            todoItem.msg = text
            if (todoItem.msg.isNotBlank()) {
                Log.d("SaveBtn", "todoitemid: " + todoItem.id)
                viewModel.addTodoItem(todoItem)
                findNavController().popBackStack()
            } else {
                val decorView = requireActivity().window.decorView
                val view = decorView.findViewById(android.R.id.content) ?: decorView.rootView
                Snackbar.make(view, "Заполните все поля", Snackbar.LENGTH_LONG)
                    .setTextColor(requireActivity().getColor(R.color.label_primary))
                    .setBackgroundTint(requireActivity().getColor(R.color.back_secondary)).show()
            }
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)) {
            Text(modifier = Modifier, text = "Сохранить", color = Color.Red)
        }
    }

    @Composable
    fun MainCard(mainText: MutableState<String>) {
        var text by remember { mainText }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(100.dp),
            shape = RoundedCornerShape(15.dp),
            elevation = 5.dp
        ) {
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                decorationBox = { innerTextField ->
                    Row(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        if (text.isEmpty()) {

                            Text("Что нужно сделать..", color = Color.LightGray)
                        }
                        innerTextField()
                    }
                },
            )
        }

    }

    /*    private fun setupListeners() {
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
                        Snackbar.make(view, "Заполните все поля", Snackbar.LENGTH_LONG)
                            .setTextColor(requireActivity().getColor(R.color.label_primary))
                            .setBackgroundTint(requireActivity().getColor(R.color.back_secondary))
                            .show()
                    }
                }
            }
        }*/

    /*    private fun openDatePicker() {
            if (todoItem.deadline != null) {
                val c = Calendar.getInstance()
                c.time = todoItem.deadline!!
            }

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, y, m, d ->
                val calendar = Calendar.getInstance()
                calendar.set(y, m, d)
                setupDate(calendar.time)
                todoItem.deadline = calendar.time
            }, year, month, day)

            datePickerDialog.show()
        }*/

    /*   private fun setupMenu() {
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
       }*/

    /* private fun setupDate(date: Date?) {
         val c = Calendar.getInstance()
         if (date != null) c.time = date

         val year = c.get(Calendar.YEAR)
         val month = c.get(Calendar.MONTH) + 1
         val day = c.get(Calendar.DAY_OF_MONTH)

         binding.tvDeadline.text = getString(R.string.date, day, month, year)
     }*/
}