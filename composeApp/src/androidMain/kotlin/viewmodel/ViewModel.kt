package viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.viewModelScope as androidViewModelScope

actual abstract class ViewModel actual constructor() : ViewModel(){
    actual val viewModelScope: CoroutineScope = androidViewModelScope
}