package com.example.todoapp.network

import com.example.todoapp.network.*
import retrofit2.Response
import retrofit2.http.*
import java.util.*


interface TodoApi {


    @GET("list")
    suspend fun getServerResponse(): TodoListResponseDto





    @GET("https://beta.mrdekk.ru/todobackend/list/{id}")//<>?????
    suspend fun getTodoById(@Path("id") id:String): TodoItemModel

   @GET ("https://beta.mrdekk.ru/todobackend/list")
   suspend fun rev(@Header("X-Last-Known-Revision") revision: Int)


    @PUT("list/{id}")
    suspend fun editTodoItemToInternet(
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
    suspend fun updateServerFromDb(@Header("X-Last-Known-Revision") revision: Int,
                                   @Body body: TodoListRequestDto
    ): Response<TodoListResponseDto>

    @POST("https://beta.mrdekk.ru/todobackend/list")
    suspend fun loadTodoItem(@Header("X-Last-Known-Revision") revision: Int, @Body body:TodoItemRequestDto)
    : Response<TodoItemResponseDto>


}