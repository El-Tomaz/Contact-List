package com.example.listadecontatos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Upsert
    suspend fun upsertContact(contact: Contato)

    @Delete
    suspend fun deleteContact(contact: Contato)

    @Query("SELECT * FROM contato ORDER BY firstName ASC")
    fun getContactsOrderedByFirstName(): Flow<List<Contato>>


    @Query("SELECT * FROM contato ORDER BY secondName ASC")
    fun getContactsOrderedBySecondName(): Flow<List<Contato>>


    @Query("SELECT * FROM contato ORDER BY phoneNumber ASC")
    fun getContactsOrderedByPhoneNumber(): Flow<List<Contato>>

}