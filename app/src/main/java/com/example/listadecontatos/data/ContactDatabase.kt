package com.example.listadecontatos.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Contato::class],
    version = 1
)
abstract class ContactDatabase : RoomDatabase() {
    abstract val dao: ContactDao
}