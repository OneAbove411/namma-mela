package com.paraminnovation.nammamela.data.repository

import com.paraminnovation.nammamela.data.dao.SeatDao
import com.paraminnovation.nammamela.data.entity.SeatEntity
import kotlinx.coroutines.flow.Flow

class SeatRepository(private val seatDao: SeatDao) {

    fun observeAll(): Flow<List<SeatEntity>> = seatDao.observeAll()

    suspend fun seedIfEmpty() {
        if (seatDao.count() > 0) return
        val seats = mutableListOf<SeatEntity>()
        // Premium: 3 rows x 10 cols, price 200
        seedSection(seats, "PREMIUM", "P", 3, 10, 200)
        // Standard: 4 rows x 10 cols, price 100
        seedSection(seats, "STANDARD", "S", 4, 10, 100)
        // Economy: 3 rows x 10 cols, price 50
        seedSection(seats, "ECONOMY", "E", 3, 10, 50)
        seatDao.upsertAll(seats)
    }

    private fun seedSection(
        out: MutableList<SeatEntity>,
        section: String,
        prefix: String,
        rows: Int,
        cols: Int,
        price: Int
    ) {
        for (r in 1..rows) {
            for (c in 1..cols) {
                out.add(
                    SeatEntity(
                        id = "$prefix$r-$c",
                        section = section,
                        rowLabel = "$prefix$r",
                        seatNumber = c,
                        price = price,
                        status = "available"
                    )
                )
            }
        }
    }

    suspend fun reserveSeats(ids: List<String>) = seatDao.setStatus(ids, "reserved")

    suspend fun resetAll() = seatDao.resetAll()
}
