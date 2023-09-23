package com.ravinada.contact.data.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ravinada.contact.data.api.response.Geo

class GeoConverter {
    @TypeConverter
    fun fromGeo(geo: Geo?): String? {
        return Gson().toJson(geo)
    }

    @TypeConverter
    fun toGeo(json: String?): Geo? {
        return Gson().fromJson(json, Geo::class.java)
    }
}
