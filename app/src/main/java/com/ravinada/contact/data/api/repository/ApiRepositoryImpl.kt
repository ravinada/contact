package com.ravinada.contact.data.api.repository

import com.ravinada.contact.data.api.request.LoginRequest
import com.ravinada.contact.data.api.response.Contact
import com.ravinada.contact.data.api.response.LoginResponse
import com.ravinada.contact.data.api.retrofit.CoResult
import com.ravinada.contact.data.api.retrofit.coResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ApiRepositoryImpl(private val api: Api) : ApiRepository {

    override suspend fun login(request: LoginRequest): CoResult<LoginResponse> =
        coResult {
            api.login(request)
        }

    override fun getContactList(): Flow<List<Contact>> {
        return flow {
            emit(api.getContactList())
        }.map {
            it
        }
    }
}
