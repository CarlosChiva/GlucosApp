package com.example.tfg.models

import java.time.LocalDateTime

data class Foreign(
    val date: LocalDateTime,
    val glucose: Int
) : java.io.Serializable