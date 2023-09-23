package com.ravinada.contact.data.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ravinada.contact.data.api.response.Company

class CompanyConverter {
    @TypeConverter
    fun fromCompany(company: Company?): String? {
        return Gson().toJson(company)
    }

    @TypeConverter
    fun toCompany(json: String?): Company? {
        return Gson().fromJson(json, Company::class.java)
    }
}
