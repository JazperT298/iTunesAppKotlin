package com.example.itunesappkotlin.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.example.itunesappkotlin.R
import com.example.ituneskotlin.utils.SharedPref
import com.squareup.picasso.Picasso

class DetailsActivity : AppCompatActivity() {
    private lateinit var context: Context
    private lateinit var artWork: ImageView
    private lateinit var trackName: TextView
    private lateinit var genre: TextView
    private lateinit var price: TextView
    private lateinit var name: String
    private lateinit var date: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        context = this
        initializeUI()
    }

    //Initializing User Interface
    private fun initializeUI() {

        //Get Item Selected Click By The User And Set The Display
        val intent = intent
        val artwork = intent.getStringExtra("artworkUrl30")
        val trackname = intent.getStringExtra("trackName")
        val genres = intent.getStringExtra("primaryGenreName")
        val prices = intent.getStringExtra("trackPrice")

        artWork = findViewById(R.id.artWork)
        trackName = findViewById(R.id.trackName)
        genre = findViewById(R.id.genre)
        price = findViewById(R.id.price)

        Picasso.get().load(artwork).fit().centerInside().into(artWork)
        trackName.text = trackname
        genre.text = genres
        price.text = prices

        checkUser()
        getSharedPref()
    }

    //Get Username and Date of Current User
    fun getSharedPref() {
        val SP = applicationContext.getSharedPreferences("NAME", 0)
        name = SP.getString("Name", null)
        date = SP.getString("Date", null)
        this@DetailsActivity.title = "$name        $date"
    }

    //Checking If There Is A Current User
    fun checkUser() {

        val Check = java.lang.Boolean.valueOf(SharedPref.readSharedSetting(applicationContext, "ClipCodes", "true"))

        val introIntent = Intent(applicationContext, LoginActivity::class.java)
        introIntent.putExtra("ClipCodes", Check)

        if (Check) {
            startActivity(introIntent)
        }
    }


    //Logout Function
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.logout -> {
                val sharedPref = SharedPref()
                SharedPref.saveSharedSetting(this@DetailsActivity, "ClipCodes", "true")
                SharedPref.SharedPrefesSAVE(applicationContext, "", "")
                sharedPref.saveUserSession(context)
                val LogOut = Intent(applicationContext, LoginActivity::class.java)
                startActivity(LogOut)
                finish()
            }
        }

        return false
    }
}
