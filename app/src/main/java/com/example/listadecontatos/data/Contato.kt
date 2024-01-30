package com.example.listadecontatos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contato(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val firstName:String,
    val secondName:String,
    val phoneNumber:String
)
