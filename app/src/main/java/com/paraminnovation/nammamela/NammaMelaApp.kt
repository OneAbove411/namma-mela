package com.paraminnovation.nammamela

import android.app.Application
import com.paraminnovation.nammamela.data.AppDatabase
import com.paraminnovation.nammamela.data.repository.CommentRepository
import com.paraminnovation.nammamela.data.repository.SeatRepository
import com.paraminnovation.nammamela.data.repository.SettingsRepository
import com.paraminnovation.nammamela.data.repository.ShowRepository
import com.paraminnovation.nammamela.data.repository.TicketRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NammaMelaApp : Application() {

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    lateinit var db: AppDatabase
    lateinit var seatRepo: SeatRepository
    lateinit var showRepo: ShowRepository
    lateinit var commentRepo: CommentRepository
    lateinit var ticketRepo: TicketRepository
    lateinit var settingsRepo: SettingsRepository

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.get(this)
        seatRepo = SeatRepository(db.seatDao())
        showRepo = ShowRepository(db.showDao(), db.castDao())
        commentRepo = CommentRepository(db.commentDao())
        ticketRepo = TicketRepository(db.ticketDao())
        settingsRepo = SettingsRepository(this)

        appScope.launch {
            seatRepo.seedIfEmpty()
            showRepo.seedIfEmpty()
            commentRepo.seedIfEmpty()
        }
    }
}
