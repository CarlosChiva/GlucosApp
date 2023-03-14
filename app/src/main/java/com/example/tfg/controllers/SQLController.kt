package com.example.tfg.controllers

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tfg.models.Datos
import com.example.tfg.models.SQLMaker
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class SQLController(context: Context) {
    var context: Context
    var sqlMaker: SQLMaker
    var sqlQueryer: SQLiteDatabase
    val MEDIDA = "medida"
    val FOREIGN_MEDIDA = "foreignMedida"

    init {
        this.context = context
        this.sqlMaker = SQLMaker(context, "db", null, 1, null)
        this.sqlQueryer = sqlMaker.writableDatabase
    }

    private fun transformDate(localDateTime: LocalDateTime): Timestamp {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, localDateTime.year)

        calendar.set(
            Calendar.MONTH,
            localDateTime.monthValue - 1
        ) // Los meses comienzan en cero en Calendar
        calendar.set(Calendar.DAY_OF_MONTH, localDateTime.dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, localDateTime.hour)
        calendar.set(Calendar.MINUTE, localDateTime.minute)
        calendar.set(Calendar.SECOND, localDateTime.second)
        calendar.set(Calendar.MILLISECOND, 0) // Establecer milisegundos en cero
        return Timestamp(calendar.timeInMillis)

    }

    fun insertIntoMedida(datos: Datos) {
        val date = transformDate(datos.fecha)
        sqlQueryer.execSQL("insert into $MEDIDA values('$date',${datos.glucosa},${datos.pick},${datos.pickIcon},${datos.alarma},${datos.CHfood},${datos.food});")
        closeAll()
    }

    fun insertIntofOREIGNMedida(medidas: List<Int>, fecha: LocalDateTime) {
        val date = transformDate(fecha)
        var indice = 1
        medidas.forEach {
            sqlQueryer.execSQL("insert into $FOREIGN_MEDIDA(fecha,id,glucosa) values('$date',$indice,$it);")
            indice++
        }

    }


    fun loadDatesMedida(): MutableList<Datos> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val result = sqlQueryer.rawQuery("select * from $MEDIDA", null)
        val datos = mutableListOf<Datos>()
        while (result.moveToNext()) {
            val fecha = dateFormat.parse(result.getString(0))
            datos.add(
                Datos(
                    fecha!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    result.getInt(1),
                    result.getInt(2),
                    getBooleans(result.getInt(3)),
                    getBooleans(result.getInt(4)),
                    result.getInt(5),
                    getBooleans(result.getInt(6))
                )
            )
        }
        result.close()
        closeAll()
        return datos


    }

    fun readAllDatesToStadistics(): MutableList<String> {
        var list = mutableListOf <String>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val result = sqlQueryer.rawQuery(
            "SELECT DISTINCT  m.fecha,m.glucosa FROM medida m UNION ALL SELECT DISTINCT f.fecha, f.glucosa FROM foreignMedida f order by 1 ASC ;",
            null
        )

        while (result.moveToNext()) {
            val fecha = dateFormat.parse(result.getString(0))
           val fechaString=fecha!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toString()

            list.add("$fechaString<${result.getInt(1)}")
        }
        result.close()
        closeAll()
        return list
    }
    //-------------------------------------------Proximamente en analisis de glucosa
    fun readDatesAtDay():MutableList<String>{
        val query = "SELECT m.fecha ,m.glucosa FROM medida m WHERE DATE(m.fecha) = CURRENT_DATE UNION ALL SELECT f.fecha, f.glucosa FROM foreignMedida f  WHERE DATE(f.fecha) = CURRENT_DATE  order by 1 asc"
        val result = sqlQueryer.rawQuery(query, null)
        var mutable = mutableListOf<String>()
        while (result.moveToNext()){
            mutable.add("${result.getString(0)}<${result.getInt(1)}")
        }
        result.close()
        closeAll()
        return mutable
    }
    fun readDatesInRange(startDate: LocalDateTime, endDate: LocalDateTime): MutableList<String> {

        val start = startDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val end = endDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val query = "SELECT m.fecha ,m.glucosa FROM medida m WHERE DATE(m.fecha) BETWEEN DATE(?) AND DATE(?)UNION ALL SELECT  f.fecha, f.glucosa FROM foreignMedida f  WHERE DATE(f.fecha) BETWEEN DATE(?) AND DATE(?)  order by 1 asc"
        val result = sqlQueryer.rawQuery(query, arrayOf(start, end,start, end))
        var mutable = mutableListOf<String>()
        while (result.moveToNext()){
            mutable.add("${result.getString(0)}<${result.getInt(1)}")
        }
        result.close()
        closeAll()
        return mutable
    }

    fun getBooleans(cursor: Int): Boolean {
        return cursor != 0 && cursor != null
    }

    private fun closeAll() {
        this.sqlQueryer.close()
        this.sqlMaker.close()
    }

}