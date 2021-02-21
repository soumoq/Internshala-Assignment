package com.example.internshalaassignment.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.internshalaassignment.R
import com.example.internshalaassignment.Util.toast
import com.example.internshalaassignment.adapter.NotesAdapter
import com.example.internshalaassignment.database.DBHelper
import com.example.internshalaassignment.viewmodel.NoteViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_note.view.*


class NotesFragment : Fragment() {

    var dbHelper: DBHelper? = null
    var noteViewModel: NoteViewModel? = null
    var notesAdapter = NotesAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note, container, false)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        view.fragment_note_list.adapter = notesAdapter
        dbHelper = DBHelper(context)
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel!!.noteList.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                notesAdapter.updateData(it)
            }
        })
        noteViewModel!!.getNotes(dbHelper!!)

        view.fragment_note_add.setOnClickListener {
            val layout = LinearLayout(context)
            layout.orientation = LinearLayout.VERTICAL

            val alert = context?.let { it1 -> AlertDialog.Builder(it1) }
            alert?.setTitle("Add note")

            val name = EditText(context)
            name.setHint("Title")
            layout.addView(name)

            val note = EditText(context)
            note.setHint("Note")
            layout.addView(note)

            alert?.setView(layout)
            alert?.setPositiveButton("add", DialogInterface.OnClickListener { dialog, which ->
                val status =
                    noteViewModel!!.insertNote(
                        dbHelper!!,
                        name.text.toString(),
                        note.text.toString()
                    )
                if (status) {
                    context?.toast("Note added")
                    noteViewModel!!.getNotes(dbHelper!!)
                    notesAdapter.notifyDataSetChanged()
                } else {
                    context?.toast("Something went wrong")
                }
            })

            alert?.setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which ->
            })

            alert?.show()
        }

        return view
    }

    fun updateNote(noteId: Int) {
        context?.toast("update $noteId")
    }

    fun deleteNote(noteId: Int) {
        val alert = context?.let { it1 -> AlertDialog.Builder(it1) }
        alert?.setTitle("Are you sure you want to delete the note")
        alert?.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
            val status = noteViewModel?.deleteNote(dbHelper!!, noteId)
            if (status == true) {
                noteViewModel!!.getNotes(dbHelper!!)
                notesAdapter.notifyDataSetChanged()
                context?.toast("Deleted")
            }
        })
        alert?.setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which ->
        })
        alert?.show()
    }

}