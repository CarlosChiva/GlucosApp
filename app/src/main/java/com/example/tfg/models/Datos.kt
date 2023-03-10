package com.example.tfg.models

import java.time.LocalDateTime
import java.util.Date

data class Datos(
    val fecha: LocalDateTime, val glucosa: Int?, val pick: Int?,
    val alarma: Boolean?,
    val CHfood: Int? , val food: Boolean?
) : java.io.Serializable