package com.example.internshalaassignment.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper : SQLiteOpenHelper {
    constructor(
        context: Context?
    ) : super(context, "userdata.db", null, 1)


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table userNotes( noteId INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, note TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop Table if exists userdata")
    }

    fun insertNote(name: String, note: String): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put("name", name)
        contentValue.put("note", note)
        val result: Long = db.insert("userNotes", null, contentValue)
        return !result.equals(-1)
    }

    fun getData(): Cursor {
        val db: SQLiteDatabase = this.writableDatabase
        return db.rawQuery("select * from userNotes", null)
    }

    fun deleteNote(id: Int): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        return db.delete("userNotes", "noteId=$id", null) > 0
    }

    fun updateNote(id: Int, name: String, note: String): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put("name", name)
        contentValue.put("note", note)
        return db.update("userNotes", contentValue, "noteId=$id", null) > 0
    }

}