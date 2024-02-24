package com.ibrahimethemsen.backend

import kotlinx.serialization.Serializable

@Serializable
data class Offset(
    val positionX : Float,
    val positionY : Float
)