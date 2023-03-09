package com.example.tfg.controllers

import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLController(
    context: Context?,
    name: String="db",
    factory: SQLiteDatabase.CursorFactory?,
    version: Int=1,
    errorHandler: DatabaseErrorHandler?
) : SQLiteOpenHelper(context, name, factory, version, errorHandler) {
    private final val MEDIDA_TABLE="medida"
    private final val FOREIGN_MEDIDA_TABLE="foreignMedida"
    override fun onCreate(sqlite: SQLiteDatabase?) {
         sqlite!!.execSQL("CREATE TABLE IF NOT EXISTS $MEDIDA_TABLE(fecha DATETIME,glucosa INTEGER,pick INTEGER, alarm BOOLEAN,CHFood INTEGER,food BOOLEAN);")
         sqlite.execSQL("CREATE TABLE IF NOT EXISTS $FOREIGN_MEDIDA_TABLE (fecha DATETIME,id INTEGER PRIMARY KEY AUTOINCREMENT, medida INTEGER,FOREIGN KEY (fecha) REFERENCES medida(fecha));")

     }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}

