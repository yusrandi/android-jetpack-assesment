package com.use.togiassesment.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE ${PokemonContract.PokemonEntry.TABLE_NAME} " +
                "(${PokemonContract.PokemonEntry._ID} INTEGER PRIMARY KEY, " +
                "${PokemonContract.PokemonEntry.COLUMN_NAME} TEXT, " +
                "${PokemonContract.PokemonEntry.COLUMN_URL} TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${PokemonContract.PokemonEntry.TABLE_NAME}")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "Pokemon.db"
        const val DATABASE_VERSION = 1
    }
}
