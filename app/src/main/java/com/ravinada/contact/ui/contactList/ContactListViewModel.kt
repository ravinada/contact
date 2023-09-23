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
    private var _originalData: List<Contact> = emptyList()

    val uiState: StateFlow<UiState<List<Contact>>> = _uiState

    init {
        fetchContactList()
    }

    fun sortContactListInAtoZ() {
        viewModelScope.launch {
            try {
                val currentUiState = uiState.value
                if (currentUiState is UiState.Success) {
                    val sortedContactList = currentUiState.data.sortedBy { it.name }
                    _uiState.value = UiState.Success(sortedContactList)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.toString(), null)
            }
        }
    }

    fun sortContactListInZtoA() {
        viewModelScope.launch {
            try {
                val currentUiState = uiState.value
                if (currentUiState is UiState.Success) {
                    val sortedContactList = currentUiState.data.sortedByDescending { it.name }
                    _uiState.value = UiState.Success(sortedContactList)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.toString(), null)
            }
        }
    }

    fun searchContactList(query: String) {
        viewModelScope.launch {
            try {
                val currentUiState = uiState.value
                if (currentUiState is UiState.Success) {
                    val filteredList = if (query.isBlank()) {
                        _originalData
                    } else {
                        currentUiState.data.filter { it.name.contains(query, ignoreCase = true) }
                    }
                    _uiState.value = UiState.Success(filteredList)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.toString(), null)
            }
        }
    }

    private fun fetchContactList() {
        viewModelScope.launch {
            apiRepository.getContactList().catch { e ->
                _uiState.value = UiState.Error(e.toString(), null)
            }.collect {
                _originalData = it
                _uiState.value = UiState.Success(it)
            }
        }
    }
}