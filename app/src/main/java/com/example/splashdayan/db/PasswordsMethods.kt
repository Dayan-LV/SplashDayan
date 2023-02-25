package com.example.splashdayan.db

import android.content.ContentValues
import android.content.Context

class PasswordsMethods (context: Context): MySQLite(context) {

    private val getPasswords = "SELECT * FROM password WHERE user_id = ?"

    fun insertPassword(password: Password): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put("user_id", password.userId)
        contentValues.put("site", password.site)
        contentValues.put("password", password.password)

        val result = db.insert("password", null, contentValues)

        db.close()

        return result != -1L
    }

    fun deletePassword(site: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete("password", "site = ?", arrayOf(site))

        db.close()

        return result != 0
    }

    fun updatePassword(password: Password): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put("password", password.password)

        val result = db.update("password", contentValues, "site = ?", arrayOf(password.site))

        db.close()

        return result != 0
    }


    fun getPasswords(userId: Int): List<Password> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(getPasswords, arrayOf(userId.toString()))

        val passwords = mutableListOf<Password>()

        if (cursor.moveToFirst()) {
            do {
                val passwordId = cursor.getInt(0)
                val site = cursor.getString(2)
                val password = cursor.getString(3)

                passwords.add(Password(passwordId, userId, site, password))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return passwords
    }

}