package com.example.listadecontatos.model

import com.example.listadecontatos.data.Contato

data class ContactState(
    val contacts: List<Contato> = emptyList(),
    val firstName: String = "",
    val secondName: String = "",
    val phoneNumber: String = "",
    val isAddingContact: Boolean = false,
    val sortType: sortType = com.example.listadecontatos.model.sortType.FIRST_NAME,
    val isEditingContact: Boolean = false,
    val currentId: Int = 0
)
