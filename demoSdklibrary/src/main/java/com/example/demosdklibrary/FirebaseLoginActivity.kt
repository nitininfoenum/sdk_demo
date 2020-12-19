package com.example.demosdklibrary

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_firebase_login.*
import java.util.regex.Pattern

class FirebaseLoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_login)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        login_btn.setOnClickListener {
            if (Validate()) {
                if (checkEmail(email_edt.text.toString())) {
                    Signup(email_edt.text.toString(), pass_edt.text.toString())
                } else Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun Signup(email: String, password: String) {
        showDialog()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    hideDialog()
                    Toast.makeText(baseContext, "Account Created Successfully", Toast.LENGTH_SHORT)
                        .show()
                    email_edt.text?.clear()
                    pass_edt.text?.clear()
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("createUserWithEmail: ", "success")
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("createUserWithEmail:", "failure" + task.exception)
                    SignIn(email, password)

                }
            }
    }

    fun SignIn(email: String, pass: String) {

        auth.signInWithEmailAndPassword(
            email, pass
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    hideDialog()
                    // Sign in success, update UI with the signed-in user's information
                    Log.e("signInWithEmail: ", "success")
                    Toast.makeText(baseContext, "Login Successfully", Toast.LENGTH_SHORT).show()
                    email_edt.text?.clear()
                    pass_edt.text?.clear()
                    val user = auth.currentUser
                } else {
                    hideDialog()
                    // If sign in fails, display a message to the user.
                    Log.w("signInWithEmail: ", "failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun Validate(): Boolean {
        if (email_edt.text?.isEmpty()!!) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
            return false
        } else if (pass_edt.text?.isEmpty()!!) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
            return false
        } else if (pass_edt.text.toString().length < 6) {
            Toast.makeText(this, "Password should have minimum 6 disit", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    private fun checkEmail(email: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }

    fun showDialog() {
        if (dialog == null) {
            dialog = ProgressDialog(this)
        }
        dialog!!.setMessage("Authentication...")
        dialog!!.show()
    }

    fun hideDialog() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }
}