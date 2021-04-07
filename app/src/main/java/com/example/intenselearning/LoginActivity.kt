package com.example.intenselearning

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    companion object{ // this companion object allows public constants to be accessed from another class
        val EXTRA_USERNAME = "username" // identifier to help us remember what the key is
        val EXTRA_PASSWORD = "password"
        // put the request code constant here
        val REQUEST_LOGIN_INFO = 1
        val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // initialize backendless --> backend as a service
        Backendless.initApp(this, Constants.APP_ID, Constants.API_KEY );

        // make the backendless call to login when they click the login button
        button_login_login.setOnClickListener{
            // extract the username and password from the edittexts
            val username = editText_login_username.text.toString()
            val password = editText_login_username.text.toString()

            // any place in the documentation you see new AsyncCallBack<Blah>,
            // the kotlin version is object : AsyncCallback<Blah> ()
            Backendless.UserService.login(username, password, object : AsyncCallback<BackendlessUser>{
                override fun handleResponse(response: BackendlessUser?) {
                    // ?. is the same as doing if(blah != null) { //then call that function or access
                    Toast.makeText(this@LoginActivity, "${response?.userId} has logged in.", Toast.LENGTH_SHORT).show()
                    // TODO bring the user to a new activity that's the "home" activity
                    // in this case, the GradeListActivity
                    val gradeListIntent = Intent(this@LoginActivity, GradeListActivity :: class.java)
                    startActivity(gradeListIntent)
                    // to close the login screen so it's not there when they click back
                    finish()
                }

                override fun handleFault(fault: BackendlessFault?) {
                    Toast.makeText(this@LoginActivity, "Something went wrong. Check the logs.", Toast.LENGTH_SHORT ).show()
                    Log.d(TAG, "handleFault" + fault?.message)
                }
            })
        }

        // make an onclicklistener for the sign up button
            button_login_signup.setOnClickListener {

                // extract the current text in the username box
                val username = editText_login_username.text.toString()

                // create an Intent that will launch the Registration Activity
                // FileName:class.java gives you acces to the class location for the Intent
                val registrationIntent = Intent(this, RegistrationActivity::class.java).apply{
                    // store that username in an "extra" in that Intent
                    putExtra(EXTRA_USERNAME, username)
                }

                // launch the new Activity
                // startActivity(registrationIntent)

                startActivityForResult(registrationIntent, REQUEST_LOGIN_INFO)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_LOGIN_INFO){
            if(resultCode == RESULT_OK){
                editText_login_username.setText(data?.getStringExtra(RegistrationActivity.EXTRA_USERNAME))
                editText_login_password.setText(data?.getStringExtra(RegistrationActivity.EXTRA_PASSWORD))
            }
        }
    }
}