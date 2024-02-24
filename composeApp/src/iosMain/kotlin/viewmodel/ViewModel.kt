package viewmodel

import kotlinx.coroutines.MainScope

actual abstract class ViewModel {
    actual val viewModelScope = MainScope()
}