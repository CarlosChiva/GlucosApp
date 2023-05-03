package com.example.tfg.controllers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.tfg.models.Data
import com.example.tfg.models.SQLMaker
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class SQLController(context: Context) {
    var context: Context
    var sqlMaker: SQLMaker
    var sqlQueryer: SQLiteDatabase
    val MEDIDA = "measure"
    val FOREIGN_MEDIDA = "foreignMeasure"

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
        calendar.set(
            Calendar.MILLISECOND,
            (Math.random() * 1000).toInt()
        ) // Establecer milisegundos en cero
        return Timestamp(calendar.timeInMillis)

    }

    //-------------------------------------------------------------Inserts
    fun insertIntoMeasure(datos: Data) {
        val date = transformDate(datos.date)
        println("inster measure")
        sqlQueryer.execSQL("insert into $MEDIDA values('$date',${datos.glucose},${datos.pick},${datos.pickIcon},${datos.alarm},${datos.CHfood},${datos.food});")

    }

    fun insertIntofOREIGNMeasure(lista: List<Pair<LocalDateTime, Int>>) {
        lista.forEach {
            val date = transformDate(it.first)
            sqlQueryer.execSQL("insert into $FOREIGN_MEDIDA(fecha,glucosa) values('$date',${it.second});")
        }

    }

    //--------------------------------------------------------------Loads
    fun loadDatesMedida(): MutableList<Data> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val result = sqlQueryer.rawQuery("select * from $MEDIDA order by fecha desc", null)
        val datos = mutableListOf<Data>()
        while (result.moveToNext()) {
            val fecha = dateFormat.parse(result.getString(0))
            datos.add(
                Data(
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

    fun readLastDatesToMeasure(): Pair<LocalDateTime, Int> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        var dateForeign: Pair<LocalDateTime, Int>? = null
        var dateMedida: Pair<LocalDateTime, Int>? = null
        var dateResult: Pair<LocalDateTime, Int>
        var result = sqlQueryer.rawQuery(
            "SELECT fecha, glucosa FROM $FOREIGN_MEDIDA " +
                    "WHERE fecha <= CURRENT_DATE " +
                    "ORDER BY fecha DESC " +
                    "LIMIT 1;",
            null
        )
        result.moveToFirst()
        println("RESULT FIRST QUERY  :      ${result.getString(0)}    ${result.getInt(1)}")
        val fechaforeign = dateFormat.parse(result.getString(0))
        val fechaString =
            fechaforeign!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                .toString()
        val glucForeign = result.getInt(1)
        dateForeign = Pair(LocalDateTime.parse(fechaString), glucForeign)

        result = sqlQueryer.rawQuery(
            "SELECT fecha, glucosa FROM $MEDIDA " +
                    "WHERE fecha <= CURRENT_DATE " +
                    "ORDER BY fecha DESC " +
                    "LIMIT 1;", null
        )
        result.moveToFirst()
        println("RESULT Second QUERY  :      ${result.getString(0)}    ${result.getInt(1)}")

        val fechaMedida = dateFormat.parse(result.getString(0))
        val fechaString2 =
            fechaMedida!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                .toString()
        val glucMedida = result.getInt(1)
        dateMedida = Pair(LocalDateTime.parse(fechaString), glucMedida)

        if (dateForeign?.first?.isAfter(dateMedida?.first)!!) {
            dateResult = dateForeign!!
        } else {
            dateResult = dateMedida!!
        }
        println(dateResult)
        result.close()
        closeAll()
        return dateResult
    }

    //-------------------------------------------Proximamente en analisis de glucosa
    fun readDatesAtDay(): List<Pair<LocalDateTime, Int>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val query =
            "SELECT m.fecha ,m.glucosa FROM $MEDIDA m WHERE DATE(m.fecha) = CURRENT_DATE UNION SELECT f.fecha, f.glucosa FROM $FOREIGN_MEDIDA f  WHERE DATE(f.fecha) = CURRENT_DATE  order by 1 asc"
        val result = sqlQueryer.rawQuery(query, null)
        var mutable = mutableListOf<Pair<LocalDateTime, Int>>()
        while (result.moveToNext()) {
            val fecha = dateFormat.parse(result.getString(0))
            val localDateTime = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

            val glucosa = result.getInt(1)
            mutable.add(localDateTime to glucosa)
        }
        result.close()
        closeAll()
        return mutable
    }

    fun readDatesInRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Pair<LocalDateTime, Int>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val start = startDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val end = endDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val query =
            " SELECT strftime('%H:%M:%S', fecha) AS hora, AVG(media_glucosa) AS media_glucosa" +
                    " FROM (" +
                    "   SELECT datetime(m.fecha) AS fecha, AVG(m.glucosa) AS media_glucosa" +
                    "   FROM $MEDIDA m" +
                    "   WHERE DATE(m.fecha) BETWEEN DATE(?) AND DATE(?)" +
                    " GROUP BY strftime('%Y-%m-%d %H:%M',m.fecha)" +
                    "   UNION ALL" +
                    "   SELECT datetime(f.fecha) AS fecha, AVG(f.glucosa) AS media_glucosa" +
                    "   FROM $FOREIGN_MEDIDA f" +
                    "   WHERE DATE(f.fecha) BETWEEN DATE(?) AND DATE(?)" +
                    " GROUP BY strftime('%Y-%m-%d %H:%M',f.fecha)" +
                    " ) AS subquery GROUP BY 1 ORDER BY 1 ASC"
        val result = sqlQueryer.rawQuery(query, arrayOf(start, end, start, end))
        var mutable = mutableListOf<Pair<LocalDateTime, Int>>()
        val formatOutput = DateTimeFormatter.ofPattern("HH:mm:ss")
        while (result.moveToNext()) {
            val hora = result.getString(0)
            if (!hora.isNullOrBlank()) {
                val fechaHora: LocalDateTime =
                    LocalTime.parse(hora, formatOutput).atDate(LocalDate.MIN)
                val glucosa = result.getInt(1)
                mutable.add(fechaHora to glucosa)
            }
        }
        result.close()
        closeAll()
        return mutable
    }

    //---------------------------------------------------------------------
    fun readAvgMorning(
    ): Int {
        val query =
            "SELECT AVG(media_glucosa) AS media_glucosa " +
                    "FROM (" +
                    "  SELECT AVG(glucosa) AS media_glucosa" +
                    "  FROM (" +
                    "    SELECT glucosa, datetime(fecha) AS fecha_hora" +
                    "    FROM $MEDIDA" +
                    "    WHERE fecha BETWEEN DATETIME('now', '-6 days') AND DATETIME('now')" +
                    "      AND strftime('%H:%M:%S', fecha) >= '06:00:00' " +
                    "      AND strftime('%H:%M:%S', fecha) <= '09:00:00'" +
                    "    UNION ALL" +
                    "    SELECT glucosa, datetime(fecha) AS fecha_hora" +
                    "    FROM $FOREIGN_MEDIDA" +
                    "    WHERE fecha BETWEEN DATETIME('now', '-6 days') AND DATETIME('now')" +
                    "      AND strftime('%H:%M:%S', fecha) >= '06:00:00' " +
                    "      AND strftime('%H:%M:%S', fecha) <= '09:00:00'" +
                    "  ) AS subquery" +
                    "  GROUP BY strftime('%Y-%m-%d %H:%M', fecha_hora)" +
                    ") AS subquery2"
        val result = sqlQueryer.rawQuery(query, null)
        var valueGlucAVG: Double = 0.0
        if (result.moveToFirst()) {
            valueGlucAVG = result.getDouble(0)
        }
        result.close()
        closeAll()
        return valueGlucAVG.toInt()
    }

    fun getInsuln_CH(): List<Array<Int>> {
        var listResult = mutableListOf<Array<Int>>()
        val query =
            "SELECT m.fecha, m.pick, m.glucosa,m.CHfood FROM $MEDIDA m WHERE pick IS NOT NULL AND m.fecha BETWEEN datetime('now', '-7 days') AND datetime('now','-0 days') order by 1 asc;"
        val result = sqlQueryer.rawQuery(query, null)
        while (result.moveToNext()) {
            var fechaComparar = result.getString(0)
            var glucosaInicial = result.getInt(2)
            var carbohidratos = result.getInt(3)
            val pick = result.getInt(1)
            val query2 =
                "SELECT fecha, AVG(glucosa)" +
                        "FROM (" +
                        "  SELECT f.fecha, f.glucosa" +
                        "  FROM $FOREIGN_MEDIDA f" +
                        "  WHERE f.fecha BETWEEN datetime('$fechaComparar', '+2 hours') AND datetime('$fechaComparar', '+125 minutes')" +
                        "  UNION ALL" +
                        "  SELECT m.fecha, m.glucosa" +
                        "  FROM $MEDIDA m" +
                        "  WHERE m.fecha BETWEEN datetime('$fechaComparar', '+2 hours') AND datetime('$fechaComparar', '+120 minutes')" +
                        ") " +
                        "GROUP BY fecha" +
                        " ORDER BY ABS(STRFTIME('%s', fecha) - STRFTIME('%s', datetime('$fechaComparar', '+2 hours')))" +
                        "LIMIT 1"
            val result2 = sqlQueryer.rawQuery(query2, null)
            if (result2.moveToFirst()) {
                val glucosaResult = result2.getInt(1)

                listResult.add(arrayOf(pick, carbohidratos, glucosaInicial, glucosaResult))
            }
        }
        return listResult

    }

    fun totalFastInsulin(): Int {
        var totalFastInsulin: Int
        val query =
            "SELECT sum(m.pick) FROM $MEDIDA m WHERE m.fecha BETWEEN datetime('now', '-7 days') AND CURRENT_DATE ;"
        val result = sqlQueryer.rawQuery(query, null)
        result.moveToFirst()
        totalFastInsulin = result.getInt(0)
        return totalFastInsulin /7
    }

    //------------------------------------------------------------------------------
    private fun getBooleans(cursor: Int): Boolean {
        return cursor != 0 && cursor != null
    }

    private fun closeAll() {
        this.sqlQueryer.close()
        this.sqlMaker.close()
    }

}