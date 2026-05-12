package com.paraminnovation.nammamela.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.paraminnovation.nammamela.data.dao.CastDao
import com.paraminnovation.nammamela.data.dao.CommentDao
import com.paraminnovation.nammamela.data.dao.SeatDao
import com.paraminnovation.nammamela.data.dao.ShowDao
import com.paraminnovation.nammamela.data.dao.TicketDao
import com.paraminnovation.nammamela.data.entity.CastEntity
import com.paraminnovation.nammamela.data.entity.CommentEntity
import com.paraminnovation.nammamela.data.entity.SeatEntity
import com.paraminnovation.nammamela.data.entity.ShowEntity
import com.paraminnovation.nammamela.data.entity.TicketEntity

@Database(
    entities = [
        ShowEntity::class,
        CastEntity::class,
        SeatEntity::class,
        CommentEntity::class,
        TicketEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun showDao(): ShowDao
    abstract fun castDao(): CastDao
    abstract fun seatDao(): SeatDao
    abstract fun commentDao(): CommentDao
    abstract fun ticketDao(): TicketDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "namma_mela.db"
            )
                // Internship-scope: destructive migration on schema change.
                // Production would write a proper Migration object.
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
        }
    }
}
