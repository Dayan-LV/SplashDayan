package com.example.splashdayan.db

import android.content.ContentValues
import android.content.Context

class UserMethods (context: Context): MySQLite(context) {

    fun insertUser(user: User): Boolean {
        val db = this.writableDatabase
        val data = ContentValues().apply {
            put("email", user.email)
            put("username", user.username)
            put("password", user.password)
            put("gender", user.gender)
            put("birth_date", user.birthDate)
            put("weight", user.weight)
        }

        return try {
            db.insertOrThrow("user", null, data)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
    }

    fun updateUser(username: String, password: String): Boolean {

        val db = this.writableDatabase

        val data = ContentValues().apply {
            put("password", password)
        }

        val selection = "username = ?"
        val selectionArgs = arrayOf(username)

        return try {
            db.update("user", data, selection, selectionArgs)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }

    }

    fun validateLogin(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val selection = "username = ? AND password = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query("user", null, selection, selectionArgs, null, null, null)

        return try {
            cursor.moveToFirst()
            cursor.count > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            cursor.close()
            db.close()
        }
    }

    fun getUser(username: String): User? {
        val db = this.readableDatabase

        val selectQuery = "SELECT * FROM user WHERE username = ?"

        val cursor = db.rawQuery(selectQuery, arrayOf(username))

        var user: User? = null

        if (cursor.moveToFirst()) {
            val userId = cursor.getInt(0)
            val email = cursor.getString(1)
            val password = cursor.getString(2)
            val gender = cursor.getString(3)
            val birthDate = cursor.getString(4)
            val weight = cursor.getDouble(5)

            user = User(
                userId = userId,
                email = email,
                username = username,
                password = password,
                gender = gender,
                birthDate = birthDate,
                weight = weight)
        }

        cursor.close()
        db.close()

        return user
    }



}