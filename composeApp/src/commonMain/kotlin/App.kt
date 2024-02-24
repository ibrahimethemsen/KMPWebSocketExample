import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import viewmodel.PositionViewModel
import kotlin.math.roundToInt

@Composable
fun App() {
    val positionViewModel = PositionViewModel().apply {
        LaunchedEffect(Unit) {
            connectToSocket()
        }
    }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }
            LaunchedEffect(Unit){
                positionViewModel.positionFlow.collect{
                    offsetX = it.x
                    offsetY = it.y
                }
            }
            Box(
                Modifier
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .clip(RoundedCornerShape(50))
                    .background(Color.Red)
                    .size(100.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                offsetX += dragAmount.x
                                offsetY += dragAmount.y
                                positionViewModel.sendPosition(offsetX, offsetY)
                            }
                        )
                    }
            )
            Text("Pozisyon ${offsetX.toInt()} y ${offsetY.toInt()}")
        }
    }
}