package com.example.tfg.controllers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.text.Selection
import com.example.tfg.models.Datos
import com.example.tfg.models.SQLMaker
import java.time.LocalDateTime

class SQLController(context: Context) {
    var context: Context
    var sqlMaker: SQLMaker
    var sqlQueryer: SQLiteDatabase
    val MEDIDA="medida"

    init {
        this.context = context
        this.sqlMaker = SQLMaker(context, "db", null, 1, null)
        this.sqlQueryer = sqlMaker.writableDatabase
    }

    fun transformDate(localDateTime: LocalDateTime): String {
        val currentYear = localDateTime.year
        val currentMonth = localDateTime.monthValue
        val currentDay = localDateTime.dayOfMonth
        val currentHour = localDateTime.hour
        val currentMinute = localDateTime.minute
        val currentSecond = localDateTime.second
        return "$currentYear-$currentMonth-$currentDay $currentHour:$currentMinute:$currentSecond"

    }

    fun insertIntoMedida(datos: Datos) {
        val date = transformDate(datos.fecha)
        sqlQueryer.execSQL("insert into $MEDIDA values('$date',${datos.glucosa},${datos.pick},${datos.alarma},${datos.CHfood},${datos.food});")

    }
fun loadDatesMedida(){
val columns= arrayOf("fecha","glucosa","pick","alarm","CHFood","food")
   val result= sqlQueryer.query(MEDIDA,columns,null ,null,null,null,null,null)

    while (result.moveToNext()){
        println("${result.getString(0)},${result.getInt(1)},${result.getInt(2)},${result.getInt(3)},${result.getInt(4)},${result.getInt(5)}")
    }
    println("Fin de la lectura de datos")


}
    fun closeAll() {
        this.sqlQueryer.close()
        this.sqlMaker.close()
    }

}