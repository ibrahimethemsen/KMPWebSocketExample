package remote

import kotlinx.coroutines.flow.Flow
import model.Offset

interface BackendService {
    suspend fun openSession()

    fun observePosition() : Flow<Offset>

    suspend fun closeSession()

    suspend fun sendPosition(position : Offset)
    companion object{
        const val PORT = 8080
        const val PATH = "/position"
    }
}