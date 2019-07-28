package com.example.itunesappkotlin.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.itunesappkotlin.MainActivity
import com.example.itunesappkotlin.R
import com.example.itunesappkotlin.utils.KeyboardHandler
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import es.dmoral.toasty.Toasty
import java.util.HashMap

class RegisterActivity : AppCompatActivity() {

    private lateinit var context: Context
    private lateinit var btnNext: Button
    private lateinit var et_Username: EditText
    private lateinit var et_Email:EditText
    private lateinit var et_Password:EditText
    private lateinit var progressDialog: ProgressDialog
    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        context = this@RegisterActivity

        initializeUI()

        KeyboardHandler.hideKeyboard(this@RegisterActivity)

    }

    private fun initializeUI() {
        //Firebase Database
        et_Username = findViewById(R.id.et_Username)
        et_Email = findViewById(R.id.et_Email)
        et_Password = findViewById(R.id.et_Password)
        btnNext = findViewById(R.id.btn_RegDone)



        btnNext.setOnClickListener(View.OnClickListener {
            checkFields()
        })
    }

    private fun checkFields() {

        auth = FirebaseAuth.getInstance()
        val txt_username = et_Username.text.toString()
        val txt_email = et_Email.text.toString()
        val txt_password = et_Password.text.toString()

        if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
            Toasty.warning(context, "All fileds are required").show()
        } else if (txt_password.length < 6) {
            Toasty.warning(context, "password must be at least 6 characters", Toast.LENGTH_SHORT).show()
        } else {
            register(txt_username, txt_email, txt_password)
        }
    }

    private fun register(username: String, email: String, password: String) {
        progressDialog = ProgressDialog(context)
        progressDialog.show()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser!!
                    val userid = firebaseUser.uid

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid)

                    val hashMap = HashMap<String, String>()
                    hashMap["id"] = userid
                    hashMap["username"] = username
                    hashMap["imageURL"] = "default"
                    hashMap["status"] = "offline"
                    hashMap["search"] = username.toLowerCase()

                    reference.setValue(hashMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressDialog.dismiss()
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    Toasty.warning(
                        context,
                        "You can't register with this email or password"
                    ).show()
                }
            }
    }


}
