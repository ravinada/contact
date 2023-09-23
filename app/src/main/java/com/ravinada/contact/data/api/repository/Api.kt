package com.ravinada.contact.data.api.repository

import com.ravinada.contact.data.api.request.LoginRequest
import com.ravinada.contact.data.api.response.Contact
import com.ravinada.contact.data.api.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    @POST("api/account/login/")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("users")
    suspend fun getContactList(): List<Contact>
}
