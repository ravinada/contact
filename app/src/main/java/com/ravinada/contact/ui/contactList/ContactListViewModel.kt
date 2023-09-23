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

    val uiState = MutableStateFlow<UiState<List<Contact>>>(UiState.Loading)
    private var contactList: List<Contact> = emptyList()

    val observeUiState: StateFlow<UiState<List<Contact>>> = uiState

    init {
        fetchContactList()
    }

    fun sortContactListInAtoZ() {
        viewModelScope.launch {
            try {
                val currentUiState = observeUiState.value
                if (currentUiState is UiState.Success) {
                    val sortedContactList = currentUiState.data.sortedBy { it.name }
                    uiState.value = UiState.Success(sortedContactList)
                }
            } catch (e: Exception) {
                uiState.value = UiState.Error(e.toString(), null)
            }
        }
    }

    fun sortContactListInZtoA() {
        viewModelScope.launch {
            try {
                val currentUiState = observeUiState.value
                if (currentUiState is UiState.Success) {
                    val sortedContactList = currentUiState.data.sortedByDescending { it.name }
                    uiState.value = UiState.Success(sortedContactList)
                }
            } catch (e: Exception) {
                uiState.value = UiState.Error(e.toString(), null)
            }
        }
    }

    fun searchContactList(query: String) {
        viewModelScope.launch {
            try {
                val currentUiState = observeUiState.value
                if (currentUiState is UiState.Success) {
                    val filteredList = if (query.isBlank()) {
                        contactList
                    } else {
                        currentUiState.data.filter { it.name.contains(query, ignoreCase = true) }
                    }
                    uiState.value = UiState.Success(filteredList)
                }
            } catch (e: Exception) {
                uiState.value = UiState.Error(e.toString(), null)
            }
        }
    }

    fun fetchContactList() {
        viewModelScope.launch {
            apiRepository.getContactList().catch { e ->
                uiState.value = UiState.Error(e.toString(), null)
            }.collect {
                contactList = it
                uiState.value = UiState.Success(it)
            }
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            apiRepository.deleteContact(contact)
        }
        fetchContactList()
    }
}