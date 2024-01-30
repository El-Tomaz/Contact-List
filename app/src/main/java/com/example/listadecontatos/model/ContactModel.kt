package com.example.listadecontatos.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listadecontatos.data.ContactDao
import com.example.listadecontatos.data.Contato
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ContactModel(
    private val dao: ContactDao
) : ViewModel() {

    private val _sortType = MutableStateFlow(sortType.FIRST_NAME)
    private val _contacts = _sortType
        .flatMapLatest {
            when (it) {
                sortType.FIRST_NAME -> dao.getContactsOrderedByFirstName()
                sortType.SECOND_NAME -> dao.getContactsOrderedBySecondName()
                sortType.PHONE_NUBER -> dao.getContactsOrderedByPhoneNumber()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(ContactState())

    val state = combine(_state, _sortType, _contacts) { state, sortType, contacts ->
        state.copy(
            contacts = contacts,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState())

    fun onEvent(event: ContactEvent) {
        when (event) {
            is ContactEvent.DeleteContact -> {
                viewModelScope.launch {
                    dao.deleteContact(event.contact)
                }
            }

            is ContactEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingContact = false
                    )
                }
            }

            is ContactEvent.EditContact -> {
                val firstName = state.value.firstName
                val lastName = state.value.secondName
                val phoneNumber = state.value.phoneNumber
                val id = state.value.currentId
                val contact = Contato(
                    id = id,
                    firstName = firstName,
                    secondName = lastName,
                    phoneNumber = phoneNumber
                )

                viewModelScope.launch {
                    dao.upsertContact(contact)
                }

                _state.update {
                    it.copy(
                        isEditingContact = false,
                        firstName = "",
                        secondName = "",
                        phoneNumber = ""
                    )
                }

            }

            is ContactEvent.SaveContact -> {
                val firstName = state.value.firstName
                val lastName = state.value.secondName
                val phoneNumber = state.value.phoneNumber

                if (firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank()) return

                val contact = Contato(
                    firstName = firstName,
                    secondName = lastName,
                    phoneNumber = phoneNumber
                )
                viewModelScope.launch {
                    dao.upsertContact(contact)
                }
                _state.update {
                    it.copy(
                        isAddingContact = false,
                        firstName = "",
                        secondName = "",
                        phoneNumber = ""
                    )
                }
            }

            is ContactEvent.SetFirstName -> {
                _state.update {
                    it.copy(
                        firstName = event.firstName
                    )
                }
            }

            is ContactEvent.SetSecondName -> {
                _state.update {
                    it.copy(
                        secondName = event.secondName
                    )
                }
            }

            is ContactEvent.SetPhoneNumber -> {
                _state.update {
                    it.copy(
                        phoneNumber = event.phoneNumber
                    )
                }
            }

            is ContactEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingContact = true
                    )
                }
            }

            is ContactEvent.SortContacts -> {
                _sortType.value = event.sortType
            }

            is ContactEvent.HideEditDialog -> {
                _state.update {
                    it.copy(
                        isEditingContact = false
                    )
                }
            }

            is ContactEvent.ShowEditDialog -> {
                val currentContact =
                    state.value.contacts.find { it.id == event.contactID }

                Log.i("Dev","Contact id = ${event.contactID}")
                Log.i("Dev","Contact id = ${ state.value.contacts}")
                if (currentContact != null) {
                    _state.update {
                        it.copy(
                            isEditingContact = true,
                            firstName = currentContact.firstName,
                            secondName = currentContact.secondName,
                            phoneNumber = currentContact.phoneNumber,
                            currentId = event.contactID

                        )

                    }
                }
            }
        }
    }
}