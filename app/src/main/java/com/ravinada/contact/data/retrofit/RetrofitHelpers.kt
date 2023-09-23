package com.ravinada.contact.data.api.retrofit

import timber.log.Timber

/**
 * Wrapper around Retrofit calls. Used to handle common exceptions
 *
 * @param T
 * @param onSuccess
 * @return
 */
suspend inline fun <T> retrofit(
    crossinline onSuccess: suspend () -> T
): T {
    return try {
        onSuccess()
    } catch (e: Exception) {
        Timber.e(e)
        throw e
    }
}