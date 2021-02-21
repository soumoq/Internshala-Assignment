package com.example.internshalaassignment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.internshalaassignment.R
import com.example.internshalaassignment.Util.toast
import com.example.internshalaassignment.fragment.NotesFragment
import com.example.internshalaassignment.model.NoteModel
import kotlinx.android.synthetic.main.layout_notes.view.*


class NotesAdapter(private val notesFragment: NotesFragment) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {


    private var allNotes: ArrayList<NoteModel> = ArrayList()

    fun updateData(notes: List<NoteModel>) {
        this.allNotes.clear()
        this.allNotes.addAll(notes)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_notes, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notesFragment, allNotes[position])
    }

    override fun getItemCount() = allNotes.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val name = view.layout_note_name
        private val note = view.layout_notes_note

        fun bind(notesFragment: NotesFragment, notes: NoteModel) {
            name.text = notes.name
            note.text = notes.note

            view.layout_note_index.text = (adapterPosition + 1).toString()
            view.layout_notes_more.setOnClickListener {
                val popupMenu = PopupMenu(it.context, it)
                popupMenu.inflate(R.menu.menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.update ->
                            notesFragment.updateNote(
                                notes.noteId,
                                notes.name.toString(),
                                notes.note.toString()
                            )
                        R.id.delete ->
                            notesFragment.deleteNote(notes.noteId)
                    }
                    true
                }
                popupMenu.show()
            }
        }
    }
}
