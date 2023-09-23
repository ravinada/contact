package com.ravinada.contact.data.api.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Entity(tableName = "contact")
@Parcelize
data class Contact(
    @field:Json(name = "id")
    @PrimaryKey val id: Int,
    @field:Json(name = "address")
    val address: Address,
    @field:Json(name = "company")
    val company: Company,
    @field:Json(name = "email")
    val email: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "phone")
    val phone: String,
    @field:Json(name = "username")
    val username: String,
    @field:Json(name = "website")
    val website: String
) : Parcelable

@Parcelize
data class Address(
    @field:Json(name = "city")
    val city: String,
    @field:Json(name = "geo")
    val geo: Geo,
    @field:Json(name = "street")
    val street: String,
    @field:Json(name = "suite")
    val suite: String,
    @field:Json(name = "zipcode")
    val zipcode: String
) : Parcelable

@Parcelize
data class Company(
    @field:Json(name = "bs")
    val bs: String,
    @field:Json(name = "catchPhrase")
    val catchPhrase: String,
    @field:Json(name = "name")
    val name: String
) : Parcelable

@Parcelize
data class Geo(
    @field:Json(name = "lat")
    val lat: String,
    @field:Json(name = "lng")
    val lng: String
) : Parcelable
