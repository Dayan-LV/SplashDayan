package com.example.splashdayan.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

open class MySQLite(context: Context): SQLiteOpenHelper(
    context, "dayan.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {

        val createUsersTable = "CREATE TABLE user (user_id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT NOT NULL UNIQUE, username TEXT NOT NULL UNIQUE, password TEXT NOT NULL, gender TEXT, birth_date DATE, weight REAL)"
        val createPasswordsTable = "CREATE TABLE password (password_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, site TEXT NOT NULL, password TEXT NOT NULL)"

        try {
            db?.apply {
                execSQL(createUsersTable)
                execSQL(createPasswordsTable)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        /*val dropTable = "DROP TABLE IF EXISTS users"

        try {
            p0?.execSQL(dropTable)
            onCreate(p0)
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
    }
}