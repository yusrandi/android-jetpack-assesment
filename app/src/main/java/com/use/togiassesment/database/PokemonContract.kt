package com.use.togiassesment.database

import android.provider.BaseColumns

class PokemonContract {
    // Tabel kontrak
    object PokemonEntry : BaseColumns {
        const val TABLE_NAME = "pokemon"
        const val _ID = BaseColumns._ID
        const val COLUMN_NAME = "name"
        const val COLUMN_URL = "url"
    }
}