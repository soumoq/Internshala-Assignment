package com.example.internshalaassignment.fragment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.internshalaassignment.R
import com.example.internshalaassignment.Util.toast
import com.example.internshalaassignment.activity.MainActivity
import com.example.internshalaassignment.dialog.ProgressDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.view.*


class LoginFragment : Fragment() {

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 1
    private lateinit var auth: FirebaseAuth
    private var progressDialog: ProgressDialog? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        //progress dialog
        progressDialog = ProgressDialog(view.context);
        progressDialog!!.setCancelable(false);

        auth = FirebaseAuth.getInstance()
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = activity?.let { GoogleSignIn.getClient(it, gso) };

        view.login_fragment_google.setOnClickListener {
            signIn()
        }

        return view
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                progressDialog?.show()
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                account!!.idToken?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                progressDialog?.dismiss()
                // Google Sign In failed, update UI appropriately
                //context?.toast(e.message.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            progressDialog?.dismiss()
            if (it.isSuccessful) {
                (activity as MainActivity?)?.gotoNotes()
            } else {
                context?.toast("Google signing in failed")
            }
        }
    }


}