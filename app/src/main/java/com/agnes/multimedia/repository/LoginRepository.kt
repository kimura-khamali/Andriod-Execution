package com.agnes.multimedia.repository

import com.agnes.multimedia.api.ApiClient
import com.agnes.multimedia.api.ApiInterface
import com.agnes.multimedia.model.LoginRequest
import com.agnes.multimedia.model.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginRepository {
    val apiClient = ApiClient.buildClient(ApiInterface::class.java)

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse>{
        return withContext(Dispatchers.IO){
            apiClient.login(loginRequest)
        }

    }
}