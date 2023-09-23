package com.ravinada.contact.data.api.response

import com.squareup.moshi.Json

data class LoginResponse(
    @field:Json(name = "message")
    val message: String,
    @field:Json(name = "payload")
    val payload: LoginPayload,
    @field:Json(name = "status_code")
    val statusCode: Int
)

data class LoginPayload(
    @field:Json(name = "email")
    val email: String?,
    @field:Json(name = "first_name")
    val firstName: String,
    @field:Json(name = "last_name")
    val lastName: String,
    @field:Json(name = "token")
    val token: String,
    @field:Json(name = "password")
    val password: String?
)
