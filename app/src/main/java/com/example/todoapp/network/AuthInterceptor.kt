package com.example.todoapp.network
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val key: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authenticatedRequest = chain.request().newBuilder()
            .header("Authorization", /*"Bearer subescheator:Karimova_L"*/key)
            .build()
        return chain.proceed(authenticatedRequest)
    }
}
