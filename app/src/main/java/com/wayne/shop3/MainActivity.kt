package com.wayne.shop3

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_first.*

class MainActivity : AppCompatActivity(),FirebaseAuth.AuthStateListener {

    private val RC_SIGNUP: Int = 100
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            AlertDialog.Builder(this)
                .setTitle("Log out")
                .setMessage("Are you sure?")
                .setPositiveButton("Confirm"){_,_ ->
                    verifyText.text = "Not log in"
                    auth.signOut()
                }
                .show()

        }

    }

    override fun onStart() {
        auth.addAuthStateListener(this)
        super.onStart()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.signUp -> {
                startActivityForResult(Intent(this,SignUpActivity::class.java),RC_SIGNUP)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onAuthStateChanged(aut: FirebaseAuth) {
        if(aut.currentUser != null){
            verifyText.setText("${aut.currentUser?.email} / ${aut.currentUser?.isEmailVerified}")
            if(aut.currentUser?.isEmailVerified!!){
                verifyEmail.visibility = View.GONE
            }else{
                verifyEmail.visibility = View.VISIBLE
            }
        }else{
            verifyText.text = "Not Log In"
            verifyEmail.visibility = View.GONE
        }

    }
}