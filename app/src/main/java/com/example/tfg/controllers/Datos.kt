package com.example.tfg.controllers

import java.util.Date

data class Datos(
    val fecha: Date, val glucosa: Int?, val pick: Int?,
    val alarma: Boolean?,
    val CHfood: Int?=5, val food: Boolean?
) : java.io.Serializable