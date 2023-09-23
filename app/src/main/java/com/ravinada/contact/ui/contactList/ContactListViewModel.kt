package com.ravinada.contact.ui.contactList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravinada.contact.data.api.repository.ApiRepository
import com.ravinada.contact.data.api.response.Contact
import com.ravinada.contact.ui.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ContactListViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Contact>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<Contact>>> = _uiState

    init {
        fetchContactList()
    }

    private fun fetchContactList() {
        viewModelScope.launch {
            apiRepository.getContactList().catch { e ->
                _uiState.value = UiState.Error(e.toString(), null)
            }.collect {
                _uiState.value = UiState.Success(it)
            }
        }
    }
}