package com.ravinada.contact.data.retrofit

/**
 * Wrapper around Coroutines that creates [CoResult]. In order to create [CoCompletable],
 * see [coCompletable]
 *
 * @param T
 * @param block
 * @return
 */
suspend inline fun <T> coResult(crossinline block: suspend () -> T): CoResult<T> =
    try {
        CoResult.Success(block())
    } catch (e: Exception) {
        CoResult.Exception(e)
    }

/**
 * Wrapper around Coroutines that creates [CoCompletable]. In order to create [CoResult],
 * see [coResult]
 *
 * @param T
 * @param block
 * @return
 */
suspend inline fun <T> coCompletable(crossinline block: suspend () -> T): CoCompletable =
    try {
        block()
        CoCompletable.Completed
    } catch (e: Exception) {
        CoCompletable.Exception(e)
    }
