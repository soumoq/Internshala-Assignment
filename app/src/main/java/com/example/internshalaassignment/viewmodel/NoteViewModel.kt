package com.example.internshalaassignment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.internshalaassignment.database.DBHelper
import com.example.internshalaassignment.model.NoteModel

class NoteViewModel : ViewModel() {
    var noteList = MutableLiveData<List<NoteModel>>()

    fun getNotes(dbHelper: DBHelper): Boolean {
        val cursor = dbHelper.getData()
        var list = ArrayList<NoteModel>()
        return if (cursor.count <= 0) {
            false
        } else {
            while (cursor.moveToNext()) {
                list.add(NoteModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2)))
                noteList.value = list
            }
            true
        }
    }

    fun insertNote(dbHelper: DBHelper, name: String, note: String): Boolean {
        return dbHelper.insertNote(name, note)
    }

    fun deleteNote(dbHelper: DBHelper, id: Int): Boolean {
        return dbHelper?.deleteNote(id)
    }

    fun updateNote(dbHelper: DBHelper, id: Int, name: String, note: String): Boolean {
        return dbHelper?.updateNote(id, name, note)
    }
}