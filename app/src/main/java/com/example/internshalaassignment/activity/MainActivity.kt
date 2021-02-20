package com.example.internshalaassignment.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.internshalaassignment.R
import com.example.internshalaassignment.fragment.LoginFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val fragment: Fragment = LoginFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, fragment).commit()
    }
}