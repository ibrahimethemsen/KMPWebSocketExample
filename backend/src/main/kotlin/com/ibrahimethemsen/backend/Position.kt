package com.ibrahimethemsen.backend

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class Position(replay : Int = 100) {
    private val _eventFlow: MutableSharedFlow<Offset> = MutableSharedFlow(replay = replay)

    val eventFlow: SharedFlow<Offset> get() = _eventFlow

    suspend fun broadcastEvent(event: Offset) {
        _eventFlow.emit(event)
    }
}