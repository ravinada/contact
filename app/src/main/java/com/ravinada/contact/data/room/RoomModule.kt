package com.ravinada.contact.data.room

import com.ravinada.contact.data.room.dao.ContactDao

class RoomModule(private val appDatabase: AppDatabase) {

    fun getContactDbDao(): ContactDao {
        return appDatabase.contactDao()
    }
}
