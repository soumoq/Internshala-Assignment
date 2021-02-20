package com.example.internshalaassignment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.internshalaassignment.R
import com.example.internshalaassignment.Util.toast
import com.google.firebase.auth.FirebaseAuth

class NotesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note, container, false)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        view.context.toast(user?.email.toString())


        return view
    }
}