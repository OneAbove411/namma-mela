package com.paraminnovation.nammamela.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "show_info")
data class ShowEntity(
    @PrimaryKey val id: Int = 1, // single-row table; always id = 1
    val name: String = "Ramayana - The Epic",
    val description: String = "A grand theatrical adaptation of the ancient Indian epic Ramayana.",
    val duration: String = "2h 30m",
    val time: String = "7:30 PM",
    val language: String = "Kannada",
    val posterPath: String? = null
)
