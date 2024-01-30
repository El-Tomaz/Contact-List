package com.example.listadecontatos.model

import com.example.listadecontatos.data.Contato

sealed interface ContactEvent {
    object SaveContact : ContactEvent
    data class SetFirstName(val firstName: String) : ContactEvent
    data class SetSecondName(val secondName: String) : ContactEvent
    data class SetPhoneNumber(val phoneNumber: String) : ContactEvent
    object ShowDialog : ContactEvent
    object HideDialog : ContactEvent
    data class SortContacts(val sortType: sortType) : ContactEvent
    data class DeleteContact(val contact: Contato) : ContactEvent

    object EditContact : ContactEvent
    data class ShowEditDialog(val contactID: Int) : ContactEvent
    object HideEditDialog : ContactEvent
}