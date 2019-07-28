package com.example.itunesappkotlin.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.itunesappkotlin.R
import com.example.itunesappkotlin.models.User
import com.example.ituneskotlin.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class DetailsActivity : AppCompatActivity() {
    private lateinit var context: Context
    private lateinit var artWork: ImageView
    private lateinit var trackName: TextView
    private lateinit var genre: TextView
    private lateinit var price: TextView
    private lateinit var name: String
    private lateinit var date: String

    private lateinit var profile_image: CircleImageView
    private lateinit var username: TextView
    private lateinit var toolbar: Toolbar


    private var firebaseUser: FirebaseUser? = null
    private lateinit var reference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        context = this
        initializeUI()
    }

    //Initializing User Interface
    private fun initializeUI() {
        profile_image = findViewById(R.id.profile_image)
        username = findViewById(R.id.username)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("")
        checkUser()
        getCurrentUser()

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


    }

    //Get Username and Date of Current User
    private fun getCurrentUser() {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.getUid())

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java!!)
                username.setText(user!!.username)
                if (user!!.imageURL.equals("default")) {
                    profile_image.setImageResource(R.drawable.note)
                } else {

                    //change this
                    Glide.with(applicationContext).load(user.imageURL).into(profile_image)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    //Checking If There Is A Current User
    fun checkUser() {

        firebaseUser = FirebaseAuth.getInstance().currentUser
        //check if user is null
        if (firebaseUser == null) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
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
                FirebaseAuth.getInstance().signOut()
                // change this code beacuse your app will crash
                startActivity(
                    Intent(
                        applicationContext,
                        LoginActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                return true
            }
        }

        return false
    }
}
