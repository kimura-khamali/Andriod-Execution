package com.agnes.multimedia.api

import com.agnes.multimedia.model.LoginRequest
import com.agnes.multimedia.model.LoginResponse
import com.agnes.multimedia.model.PhotoResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface {
    @Multipart
    @POST("/selfie")
    suspend fun uploadPhoto(
        @Part caption : MultipartBody.Part,
        @Part image : MultipartBody.Part
    ):Response<PhotoResponse>

    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}