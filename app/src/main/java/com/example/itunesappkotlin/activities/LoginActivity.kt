package com.example.itunesappkotlin.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.itunesappkotlin.MainActivity
import com.example.itunesappkotlin.R
import com.example.ituneskotlin.utils.SharedPref
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var imageView: ImageView
    private lateinit var currentTime: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initializeUI()
    }

    //Initializing User Interface
    private fun initializeUI() {
        name = findViewById(R.id.name)
        password = findViewById(R.id.password)
        imageView = findViewById(R.id.image)
        login = findViewById(R.id.login)

        val someHandler = Handler(mainLooper)
        someHandler.postDelayed(object : Runnable {
            override fun run() {
                val c = Calendar.getInstance()
                val sdf = SimpleDateFormat("hh:mm MM-dd")
                currentTime = sdf.format(c.time)
                someHandler.postDelayed(this, 1000)
            }
        }, 10)
        login.setOnClickListener(View.OnClickListener {
            SharedPref.saveSharedSetting(this@LoginActivity, "ClipCodes", "false")
            SharedPref.SharedPrefesSAVE(applicationContext, name.getText().toString(), currentTime)
            val ImLoggedIn = Intent(applicationContext, MainActivity::class.java)
            startActivity(ImLoggedIn)
            finish()
        })
    }
}
