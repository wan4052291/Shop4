package com.wayne.shop3

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.view.*

class SignUpActivity : AppCompatActivity() {
    private val RC_GOOGLE_SIGN_IN: Int = 101
    private val auth = FirebaseAuth.getInstance()
    private lateinit var alertDialog : AlertDialog.Builder
    private lateinit var googleSignInClient:GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
        google_sign_in.setOnClickListener{
            startActivityForResult(googleSignInClient.signInIntent,RC_GOOGLE_SIGN_IN)
        }
        alertDialog = AlertDialog.Builder(this)
        signup.setOnClickListener {
            val email = edEmail.text.toString()
            val password = edPassword.text.toString()
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        alertDialog.setTitle("Sign Up")
                            .setMessage("Account created")
                            .setPositiveButton("OK"){ _, _ ->
                                finish()
                            }
                            .show()
                    }else{
                        alertDialog.setTitle("Sign Up")
                            .setMessage(it.exception?.message)
                            .setNegativeButton("Cancel",null)
                            .show()
                    }
                }
        }
        login.setOnClickListener {
            val email = edEmail.text.toString()
            val password = edPassword.text.toString()
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        alertDialog.setTitle("Log in")
                            .setMessage("Welcome")
                            .setPositiveButton("OK"){_,_ ->
                                finish()
                            }
                            .show()
                    }else{
                        alertDialog.setTitle("Log in")
                            .setMessage(it.exception?.message)
                            .setNegativeButton("Cancel",null)
                            .show()
                    }
                }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            Log.d("TAG", "onActivityResult: ${account?.id}")
            val credential = GoogleAuthProvider.getCredential(account?.idToken,null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        setResult(Activity.RESULT_OK)
                        finish()
                    }else{
                        Log.d("TAG", "onActivityResult: ${it.exception?.message}")
                        Snackbar.make(main_view,"Firebase Authentication Error",Snackbar.LENGTH_LONG).show()
                    }
                }
        }
    }
}