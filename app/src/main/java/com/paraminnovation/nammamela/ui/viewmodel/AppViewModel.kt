package com.paraminnovation.nammamela.ui.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.paraminnovation.nammamela.NammaMelaApp
import com.paraminnovation.nammamela.data.entity.CastEntity
import com.paraminnovation.nammamela.data.entity.ShowEntity
import com.paraminnovation.nammamela.data.entity.TicketEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(private val app: NammaMelaApp) : AndroidViewModel(app) {

    val show = app.showRepo.observeShow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val cast = app.showRepo.observeCast()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val seats = app.seatRepo.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val comments = app.commentRepo.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val tickets = app.ticketRepo.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val userName = app.settingsRepo.userName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val razorpayKey = app.settingsRepo.razorpayKey
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val geminiKey = app.settingsRepo.geminiKey
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _selectedSeats = MutableStateFlow<Set<String>>(emptySet())
    val selectedSeats: StateFlow<Set<String>> = _selectedSeats.asStateFlow()

    /** Snapshot of the in-flight booking the user is paying for. */
    private var pendingBooking: PendingBooking? = null

    /** Emits the ticket ID just inserted after Razorpay success, so the UI can navigate. */
    private val _newTicketId = MutableStateFlow<Long?>(null)
    val newTicketId: StateFlow<Long?> = _newTicketId.asStateFlow()

    fun toggleSeat(seatId: String) {
        val currentStatus = seats.value.firstOrNull { it.id == seatId }?.status
        if (currentStatus == "reserved") return
        _selectedSeats.value = if (_selectedSeats.value.contains(seatId))
            _selectedSeats.value - seatId
        else
            _selectedSeats.value + seatId
    }

    fun clearSelection() { _selectedSeats.value = emptySet() }

    fun selectedTotal(): Int {
        val byId = seats.value.associateBy { it.id }
        return _selectedSeats.value.sumOf { byId[it]?.price ?: 0 }
    }

    /** Called from SeatMapScreen right BEFORE invoking Razorpay so we have a snapshot. */
    fun beginPayment() {
        pendingBooking = PendingBooking(
            seatIds = _selectedSeats.value.toList(),
            total = selectedTotal(),
            playName = show.value?.name ?: "",
            language = show.value?.language ?: "",
            showTime = show.value?.time ?: "",
            customerName = userName.value
        )
    }

    /** Called by MainActivity from Razorpay onPaymentSuccess. */
    fun onPaymentSuccess(paymentId: String?) {
        val p = pendingBooking ?: return
        if (p.seatIds.isEmpty()) return
        viewModelScope.launch {
            app.seatRepo.reserveSeats(p.seatIds)
            val ticket = TicketEntity(
                playName = p.playName,
                language = p.language,
                showTime = p.showTime,
                seatList = p.seatIds.joinToString(", "),
                seatCount = p.seatIds.size,
                totalAmount = p.total,
                paymentId = paymentId,
                customerName = p.customerName
            )
            val id = app.ticketRepo.insert(ticket)
            _selectedSeats.value = emptySet()
            pendingBooking = null
            _newTicketId.value = id
        }
    }

    fun consumeNewTicket() { _newTicketId.value = null }

    fun onPaymentCancelled() { pendingBooking = null }

    fun resetAllSeats() {
        viewModelScope.launch { app.seatRepo.resetAll() }
    }

    fun updateShow(s: ShowEntity) { viewModelScope.launch { app.showRepo.updateShow(s) } }
    fun updateCast(c: CastEntity) { viewModelScope.launch { app.showRepo.updateCast(c) } }

    fun postComment(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val name = userName.value ?: "Audience Member"
            app.commentRepo.post(name, text.trim())
        }
    }

    fun setUserName(name: String) { viewModelScope.launch { app.settingsRepo.setUserName(name.trim()) } }
    fun setRazorpayKey(k: String) { viewModelScope.launch { app.settingsRepo.setRazorpayKey(k.trim()) } }
    fun setGeminiKey(k: String) { viewModelScope.launch { app.settingsRepo.setGeminiKey(k.trim()) } }

    suspend fun verifyPin(pin: String): Boolean = app.settingsRepo.verifyPin(pin)
    fun setPin(pin: String) { viewModelScope.launch { app.settingsRepo.setPin(pin) } }

    fun ticketById(id: Long) = app.ticketRepo.observeById(id)

    private data class PendingBooking(
        val seatIds: List<String>,
        val total: Int,
        val playName: String,
        val language: String,
        val showTime: String,
        val customerName: String?
    )

    class Factory(private val app: NammaMelaApp) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return AppViewModel(app) as T
        }
    }
}
