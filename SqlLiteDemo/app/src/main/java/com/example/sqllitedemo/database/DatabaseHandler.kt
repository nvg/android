package com.example.sqllitedemo.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.sqllitedemo.model.User

class DatabaseHandler(
    context: Context
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "user_db"
        private const val TABLE = "user_data"
        private const val KEY_ID = "user_name"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE user_data(user_name TEXT PRIMARY KEY, email TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS user_data")
    }

    fun getUsers(): ArrayList<User> {
        val result = ArrayList<User>()
        with(this.readableDatabase) {
            with(rawQuery("SELECT * FROM user_data", null)) {
                while (moveToNext()) {
                    val user = User(getString(0), getString(1))
                    Log.e("DB", "Got user $user")
                    result.add(user)
                }
            }
        }
        return result
    }

    fun addUser(user: User) {
        val contentValues = toContentValues(user)

        with(this.writableDatabase) {
            insert("user_data", null, contentValues)
        }
    }

    fun updateUser(user: User) {
        val contentValues = toContentValues(user)

        with(this.writableDatabase) {
            update("user_data", contentValues, "user_name = '${user.name}'", null)
        }
    }

    fun deleteUser(user: User) {
        with(this.writableDatabase) {
            delete("user_data", "user_name = '${user.name}'", null)
        }
    }

    private fun toContentValues(user: User): ContentValues {
        val contentValues = ContentValues()
        contentValues.put("user_name", user.name)
        contentValues.put("email", user.email)
        return contentValues
    }

}