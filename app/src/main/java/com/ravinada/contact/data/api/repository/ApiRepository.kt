package com.ravinada.contact.data.api.repository

import com.ravinada.contact.data.api.request.LoginRequest
import com.ravinada.contact.data.api.response.Contact
import com.ravinada.contact.data.api.response.LoginResponse
import com.ravinada.contact.data.api.retrofit.CoResult
import kotlinx.coroutines.flow.Flow

interface ApiRepository {

    suspend fun login(request: LoginRequest): CoResult<LoginResponse>

    fun getContactList(): Flow<List<Contact>>
}