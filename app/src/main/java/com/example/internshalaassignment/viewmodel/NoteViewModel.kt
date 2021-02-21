package com.example.internshalaassignment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.internshalaassignment.database.DBHelper
import com.example.internshalaassignment.model.NoteModel

class NoteViewModel : ViewModel() {
    var noteList = MutableLiveData<List<NoteModel>>()

    fun getNotes(dbHelper: DBHelper) {
        val cursor = dbHelper.getData()
        var list = ArrayList<NoteModel>()
        while (cursor.moveToNext()) {
            list.add(NoteModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2)))
            noteList.value = list
        }
    }

    fun insertNote(dbHelper: DBHelper, name: String, note: String): Boolean {
        return dbHelper.insertNote(name, note)
    }
}