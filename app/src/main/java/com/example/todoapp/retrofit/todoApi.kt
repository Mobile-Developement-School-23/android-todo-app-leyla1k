package com.example.todoapp.retrofit

import com.example.todoapp.network.*
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.http.*
import java.util.*


interface todoApi {

    @GET("https://beta.mrdekk.ru/todobackend/list/{id}")//<>?????
    suspend fun getTodoById(@Path("id") id:String): TodoItemModel

    @PUT("list/{id}")
    suspend fun editTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: UUID,
        @Body body: TodoItemRequestDto
    ): Response<TodoItemResponseDto>

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: UUID
    ): Response<TodoItemResponseDto>


    @GET("https://beta.mrdekk.ru/todobackend/list")
    suspend fun downloadTodoList(): Response<TodoListResponseDto>

//пока не юзаю
    @PATCH("https://beta.mrdekk.ru/todobackend/list")
    suspend fun updateTodoListFromInternet( @Header("X-Last-Known-Revision") revision: Int,
                                            @Body body: TodoListRequestDto
    ): Response<TodoListResponseDto>

    @POST("https://beta.mrdekk.ru/todobackend/list")
    suspend fun loadTodoItem(@Header("X-Last-Known-Revision") revision: Int, @Body item:TodoItemRequestDto)
    : Response<TodoItemResponseDto>
    /*@Body body: TodoItemRequestDto
    ): Response<TodoItemResponseDto>//внутри скобок где параметры*/

}