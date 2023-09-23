package com.ravinada.contact.data.api.repository

import com.ravinada.contact.data.api.response.Contact
import com.ravinada.contact.data.room.dao.ContactDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ApiRepositoryImpl(private val api: Api, private val contactDao: ContactDao) : ApiRepository {

    override suspend fun fetchContact() {
        val storedContacts = contactDao.getAllContacts().firstOrNull()
        if (storedContacts.isNullOrEmpty()) {
            try {
                val contacts = api.getContactList()
                contactDao.insertContacts(contacts)
            } catch (e: Exception) {
                // Handle the exception
            }
        }
    }

    override fun getContactList(): Flow<List<Contact>> {
        return contactDao.getAllContacts()
    }

    override suspend fun deleteContact(contact: Contact) {
        contactDao.deleteContact(contact)
    }
}
