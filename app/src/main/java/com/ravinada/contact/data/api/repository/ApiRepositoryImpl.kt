package com.ravinada.contact.data.api.repository

import com.ravinada.contact.data.api.response.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ApiRepositoryImpl(private val api: Api) : ApiRepository {

    override fun getContactList(): Flow<List<Contact>> {
        return flow {
            emit(api.getContactList())
        }.map {
            it
        }
    }
}
