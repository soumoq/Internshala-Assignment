package com.example.internshalaassignment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.internshalaassignment.R
import com.example.internshalaassignment.model.NoteModel
import kotlinx.android.synthetic.main.layout_notes.view.*


class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {


    private var productPricing: ArrayList<NoteModel> = ArrayList()

    fun updateData(pricing: List<NoteModel>) {
        this.productPricing.clear()
        this.productPricing.addAll(pricing)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_notes, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productPricing[position])
    }

    override fun getItemCount() = productPricing.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val name = view.layout_note_name
        private val note = view.layout_notes_note

        fun bind(notes: NoteModel) {
            name.text = notes.name
            note.text = notes.note

        }
    }
}
