package com.example.tfg.models

import java.time.LocalDateTime
data class Data(
    val date: LocalDateTime,
    val glucose: Int?,
    val pick: Int?,
    val pickIcon: Boolean?,
    val alarm: Boolean?,
    val CHfood: Int?,
    val food: Boolean?
) : java.io.Serializable