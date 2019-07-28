package com.example.itunesappkotlin.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.itunesappkotlin.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import es.dmoral.toasty.Toasty

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var context: Context
    private lateinit var input_Email: EditText
    private  lateinit var btn_Reset: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        context = this@ResetPasswordActivity
        initializeUI()
    }

    private fun initializeUI () {
        input_Email = findViewById(R.id.input_Email)
        btn_Reset = findViewById(R.id.btn_Reset)

        firebaseAuth = FirebaseAuth.getInstance()


        btn_Reset.setOnClickListener(View.OnClickListener {
            resetUserPassword()
        })

    }

    private fun resetUserPassword() {
        progressDialog = ProgressDialog(context)
        progressDialog.show()
        val email = input_Email.text.toString()

        if (email == "") {
            Toasty.warning(this@ResetPasswordActivity, "All fileds are required!").show()
        } else {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    Toasty.info(this@ResetPasswordActivity, "Please check you Email").show()
                    startActivity(Intent(this@ResetPasswordActivity, LoginActivity::class.java))
                } else {
                    val error = task.exception!!.message
                    Toasty.error(this@ResetPasswordActivity, error!!).show()
                }
            }
        }
    }
}
