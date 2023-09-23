package com.ravinada.contact.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ravinada.contact.data.api.response.Contact
import com.ravinada.contact.data.room.converter.AddressConverter
import com.ravinada.contact.data.room.converter.CompanyConverter
import com.ravinada.contact.data.room.converter.GeoConverter
import com.ravinada.contact.data.room.dao.ContactDao

@Database(entities = [Contact::class], version = 2)
@TypeConverters(CompanyConverter::class, AddressConverter::class, GeoConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}
