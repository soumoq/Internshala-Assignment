package com.example.internshalaassignment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.internshalaassignment.R
import com.example.internshalaassignment.Util.toast
import com.example.internshalaassignment.database.DBHelper
import com.example.internshalaassignment.model.NoteModel
import com.example.internshalaassignment.viewmodel.NoteViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.android.synthetic.main.fragment_note.view.*

class NotesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note, container, false)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val dbHelper = DBHelper(context)
        val noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.noteList.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                for (noteModel in it) {
                    context?.toast("${it.size}")
                    view.test.append("${noteModel.note}\n\n\n")
                }
            }
        })
        noteViewModel.getNotes(dbHelper)


        view.fragment_note_add.setOnClickListener {
            val flag = noteViewModel.insertNote(
                dbHelper,
                fragment_note_name.text.toString(),
                fragment_note_note.text.toString()
            )
            if (flag) {
                noteViewModel.getNotes(dbHelper)
            }
        }





        return view
    }
}