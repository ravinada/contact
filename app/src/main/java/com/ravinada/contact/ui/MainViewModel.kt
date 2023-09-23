package com.ravinada.contact.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravinada.contact.data.api.repository.ApiRepository
import kotlinx.coroutines.launch

class MainViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    fun fetchContact() {
        viewModelScope.launch {
            apiRepository.fetchContact()
        }
    }
}
