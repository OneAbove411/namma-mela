package com.paraminnovation.nammamela.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seats")
data class SeatEntity(
    @PrimaryKey val id: String,       // e.g. "P1-3"
    val section: String,              // "PREMIUM", "STANDARD", "ECONOMY"
    val rowLabel: String,             // e.g. "P1"
    val seatNumber: Int,              // 1..10
    val price: Int,                   // 200/100/50
    val status: String = "available", // "available" or "reserved"
    val updatedAt: Long = System.currentTimeMillis()
)
