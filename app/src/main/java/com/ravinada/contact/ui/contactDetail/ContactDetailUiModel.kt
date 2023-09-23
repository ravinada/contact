package com.ravinada.contact.ui.contactDetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactDetailUiModel(
    val name: String
) : Parcelable
