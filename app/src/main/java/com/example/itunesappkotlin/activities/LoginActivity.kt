package com.example.itunesappkotlin.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.itunesappkotlin.MainActivity
import com.example.itunesappkotlin.R
import com.example.ituneskotlin.utils.SharedPref
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.loopj.android.http.RequestParams
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var context: Context
    //Widgets
    private lateinit var tv_CreateAccount: TextView
    private lateinit var tv_ForgotPassword:TextView
    private lateinit var btn_Next: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword:EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var loginView: View

    private lateinit var progressDialog: ProgressDialog


    //Firebase
    lateinit var auth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        context = this@LoginActivity
        initializeUI()

        checkUser()
    }

    //Initializing User Interface
    private fun initializeUI() {
        tv_CreateAccount = findViewById(R.id.tv_CreateAccount)
        tv_ForgotPassword = findViewById(R.id.tv_ForgotPassword)
        progressBar = findViewById(R.id.login_progress)
        loginView = findViewById(R.id.email_login_form)
        tv_CreateAccount = findViewById(R.id.tv_CreateAccount)
        etEmail = findViewById(R.id.input_Username)
        etPassword = findViewById(R.id.et_LoginPassword)

        auth = FirebaseAuth.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser

        btn_Next = findViewById(R.id.btn_Next)
        btn_Next.setOnClickListener(View.OnClickListener {
            loginUser()
        })
        tv_CreateAccount.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        })
        tv_ForgotPassword.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@LoginActivity, ResetPasswordActivity::class.java))
        })

    }

    //Login from  Firebase
    private fun loginUser() {
        progressDialog = ProgressDialog(context)
        progressDialog.show()
        val txt_email = etEmail.text.toString()
        val txt_password = etPassword.text.toString()

        if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
            Toast.makeText(this@LoginActivity, "All fileds are required", Toast.LENGTH_SHORT).show()
        } else {

            auth.signInWithEmailAndPassword(txt_email, txt_password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressDialog.dismiss()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Authentication failed!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun checkUser() {
        //check if user is null
        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
