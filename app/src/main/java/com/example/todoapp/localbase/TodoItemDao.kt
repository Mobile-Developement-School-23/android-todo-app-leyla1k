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

   /* @Insert
    suspend fun insertRevision(rev: DbRevision)*/
    @Query("SELECT * FROM items ")
     fun getTodoListFlow(): Flow<List<TodoDataItem>>

    @Query("DELETE FROM items")
    suspend fun deleteList()


    @Query("DELETE FROM items WHERE id=:TodoItemId")
    suspend fun deleteTodoItem(TodoItemId: String)


    @Update
    suspend fun updateTodoItem(note: TodoDataItem)

    @Query("SELECT * FROM items")
    suspend fun getTodoItem():TodoDataItem


    @Query("SELECT * from revision WHERE id = :id")
    suspend fun getRevision(id: Int = 1): DbRevision

    @Update
    suspend fun updateRevision(revision: DbRevision)
/////////////////////////////////////////////////ьудет ли запускаться на другом устройстве если введено вручную хммммм очевидно нет, ввести надо актуальную ревизию


}