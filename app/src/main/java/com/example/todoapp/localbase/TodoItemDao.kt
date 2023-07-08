package com.example.todoapp.localbase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoItemDao {
    @Insert
     suspend fun insertTodoItem(item: TodoDataItem)


    @Query("SELECT * FROM items ")
     fun getTodoListFlow(): Flow<List<TodoDataItem>> //тут может быть флоу

    @Query("DELETE FROM items")
    suspend fun deleteList()


    @Query("DELETE FROM items WHERE id=:TodoItemId")
    suspend fun deleteTodoItem(TodoItemId: String)


    @Update
    suspend fun updateNote(note: TodoDataItem)

    @Query("SELECT * FROM items")
    suspend fun getTodoItem():TodoDataItem


    /*@Query ("UPDATE items SET isDone = NOT isDone WHERE id==:noteId")
    fun updateDoneStatus(noteId : Int)*/

}