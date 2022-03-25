package com.example.parstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // check if there's a user logged in
        if (ParseUser.getCurrentUser() != null)
            goToMainActivity() // if there is, take them to MainActivity


        // get login button -> get the entered username and pw log in
        findViewById<Button>(R.id.btn_login).setOnClickListener{
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            loginUser(username, password)
        }

        // get signup button -> get the entered username and pw sign up
        findViewById<Button>(R.id.btn_signup).setOnClickListener{
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            signUpUser(username, password)
        }
    }

    private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {
                Log.i(TAG, "Successfully logged in user")
                // navigate to main activity
                goToMainActivity()
            } else {
                Log.e(TAG,"error logging in: $e")
                Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT).show()
            }})
        )
    }

    private fun signUpUser(username: String, password: String){
        // Create the ParseUser
        val user = ParseUser()

        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) {
                // Hooray! Let them use the app now.
                goToMainActivity()
                Toast.makeText(this, "Successfully signed up!", Toast.LENGTH_SHORT).show()
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Log.e(TAG, "error signing up: $e")
                Toast.makeText(this, "Error signing up", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        //finish()
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}

