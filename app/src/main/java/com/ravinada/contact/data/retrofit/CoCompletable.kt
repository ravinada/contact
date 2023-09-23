package com.ravinada.contact.data.api.retrofit

/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 * Modifications Copyright (C) 2021 Davor Maric
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain exception copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Represents exception value of one of two possible types (exception disjoint union).
 * Instances of [CoCompletable] are either an instance of [Exception] or [Completed].
 *
 * @see Exception
 * @see Completed
 */
sealed class CoCompletable {
    /** * Represents the exception side of [CoCompletable] class. */
    data class Exception(val exception: kotlin.Exception) : CoCompletable() {
        val value: kotlin.Exception get() = exception
    }

    /** * Represents the completed side of [CoCompletable] class. */
    object Completed : CoCompletable()

    val isException get() = this is Exception
    val isCompleted get() = this is Completed

    fun exception(a: kotlin.Exception) = Exception(a)
    fun complete() = Completed

    fun either(fnL: (kotlin.Exception) -> Any, fnR: () -> Any): Any =
        when (this) {
            is Exception -> fnL(exception)
            is Completed -> fnR()
        }

    override fun toString(): String {
        if (this is Exception) {
            return this.value.toString()
        }
        return super.toString()
    }
}
