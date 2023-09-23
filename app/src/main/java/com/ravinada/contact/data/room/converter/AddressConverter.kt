package com.ravinada.contact.data.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ravinada.contact.data.api.response.Address

class AddressConverter {
    @TypeConverter
    fun fromAddress(address: Address?): String? {
        return Gson().toJson(address)
    }

    @TypeConverter
    fun toAddress(json: String?): Address? {
        return Gson().fromJson(json, Address::class.java)
    }
}
