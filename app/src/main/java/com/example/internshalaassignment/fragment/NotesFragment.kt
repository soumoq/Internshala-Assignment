package com.example.internshalaassignment.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.internshalaassignment.R
import com.example.internshalaassignment.Util.toast
import com.example.internshalaassignment.activity.MainActivity
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
            showDialog("Add note")
        }

        view.fragment_note_account.setOnClickListener {
            val alert = context?.let { it1 -> AlertDialog.Builder(it1) }
            alert?.setTitle(user?.displayName.toString())

            val layout = LinearLayout(context)
            layout.orientation = LinearLayout.VERTICAL

            val name = TextView(context)
            name.setText("Email: ${user?.email.toString()}")
            alert?.setView(layout)
            layout.addView(name)
            alert?.setPositiveButton("Logout", DialogInterface.OnClickListener { dialog, which ->
                auth.signOut()
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            })
            alert?.setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which ->
            })
            alert?.show()
        }

        return view
    }

    fun updateNote(noteId: Int, name: String, note: String) {
        showDialog("Update note", noteId, name, note)
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


    private fun showDialog(
        title: String,
        id: Int = 0,
        noteTitle: String = "",
        noteText: String = ""
    ) {
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        val alert = context?.let { it1 -> AlertDialog.Builder(it1) }
        alert?.setTitle(title)

        val name = EditText(context)
        name.setHint("Title")
        name.setText(noteTitle)
        layout.addView(name)

        val note = EditText(context)
        note.setHint("Note")
        note.setText(noteText)
        layout.addView(note)

        alert?.setView(layout)
        alert?.setPositiveButton("add", DialogInterface.OnClickListener { dialog, which ->
            if (name.text.isNotEmpty() && note.text.isNotEmpty()) {
                if (title.equals("Add note")) {
                    val status = noteViewModel!!.insertNote(
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
                } else {
                    val status = noteViewModel!!.updateNote(
                        dbHelper!!,
                        id,
                        name.text.toString(),
                        note.text.toString()
                    )
                    if (status) {
                        context?.toast("Updated")
                        noteViewModel!!.getNotes(dbHelper!!)
                        notesAdapter.notifyDataSetChanged()
                    }
                }
            } else {
                context?.toast("Enter valid input")
            }
        })

        alert?.setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which ->
        })

        alert?.show()
    }

}