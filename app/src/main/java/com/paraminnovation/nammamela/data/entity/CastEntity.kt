package com.paraminnovation.nammamela.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cast_info")
data class CastEntity(
    @PrimaryKey val id: Int = 1, // single-row table
    val leadName: String = "Rajesh Kumar",
    val leadRole: String = "as Lord Rama",
    val comedianName: String = "Ganesh Rao",
    val comedianRole: String = "as Hanuman's Friend",
    val singerName: String = "Lakshmi Devi",
    val singerRole: String = "Classical Vocalist"
)
