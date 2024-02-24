package viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import model.Offset

import remote.BackendService
import remote.BackendServiceImpl

class PositionViewModel : ViewModel() {
    private val service : BackendService = BackendServiceImpl()
    private val _positionFlow: MutableStateFlow<androidx.compose.ui.geometry.Offset> =
        MutableStateFlow(androidx.compose.ui.geometry.Offset(0f,0f))
    val positionFlow: StateFlow<androidx.compose.ui.geometry.Offset> get() = _positionFlow

    fun connectToSocket(){
        viewModelScope.launch {
            service.openSession()
            service.observePosition().collect{offset ->
                _positionFlow.value = androidx.compose.ui.geometry.Offset(offset.positionX,offset.positionY)
            }
        }
    }

    fun sendPosition(
        positionX : Float,
        positionY : Float,
    ){
        viewModelScope.launch {
            service.sendPosition(Offset(positionX, positionY))
        }
    }
}