package com.paraminnovation.nammamela.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val playName: String,
    val language: String,
    val showTime: String,
    val seatList: String,        // CSV of seat IDs
    val seatCount: Int,
    val totalAmount: Int,        // INR
    val paymentId: String?,      // Razorpay payment ID; null in dev/no-payment paths
    val customerName: String?,
    val createdAt: Long = System.currentTimeMillis()
)
