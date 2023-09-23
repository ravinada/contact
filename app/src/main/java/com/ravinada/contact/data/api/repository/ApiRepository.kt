package com.ravinada.contact.data.api.repository

import com.ravinada.contact.data.api.response.Contact
import kotlinx.coroutines.flow.Flow

interface ApiRepository {

    fun getContactList(): Flow<List<Contact>>

    suspend fun fetchContact()

    suspend fun deleteContact(contact: Contact)
}