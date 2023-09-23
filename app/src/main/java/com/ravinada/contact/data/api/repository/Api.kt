package com.ravinada.contact.data.api.repository

import com.ravinada.contact.data.api.response.Contact
import retrofit2.http.GET

interface Api {

    @GET("users")
    suspend fun getContactList(): List<Contact>
}
