package com.example.tfg.models

import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLMaker(
    context: Context?,
    name: String="db",
    factory: SQLiteDatabase.CursorFactory?,
    version: Int=1,
    errorHandler: DatabaseErrorHandler?
) : SQLiteOpenHelper(context, name, factory, version, errorHandler) {
     val MEDIDA_TABLE="medida"
     val FOREIGN_MEDIDA_TABLE="foreignMedida"
    override fun onCreate(sqlite: SQLiteDatabase?) {
         sqlite!!.execSQL("CREATE TABLE IF NOT EXISTS $MEDIDA_TABLE(fecha DATETIME PRIMARY KEY,glucosa INTEGER,pick INTEGER,pickIcon BOOLEAN, alarm BOOLEAN,CHFood INTEGER,food BOOLEAN);")
         sqlite.execSQL("CREATE TABLE IF NOT EXISTS $FOREIGN_MEDIDA_TABLE (fecha DATETIME PRIMARY KEY, glucosa INTEGER);")

     }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}

