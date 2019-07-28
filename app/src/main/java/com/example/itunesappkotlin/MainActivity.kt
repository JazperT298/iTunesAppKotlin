package com.example.itunesappkotlin

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.itunesappkotlin.activities.DetailsActivity
import com.example.itunesappkotlin.activities.LoginActivity
import com.example.itunesappkotlin.adapters.SongAdapter
import com.example.itunesappkotlin.models.SongModel
import com.example.itunesappkotlin.models.User
import com.example.ituneskotlin.utils.Debugger
import com.example.ituneskotlin.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.loopj.android.http.RequestParams
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.ArrayList

class MainActivity : AppCompatActivity(), SongAdapter.OnItemClickListener {


    private lateinit var context: Context
    private lateinit var view: View
    private var name: String? = null
    private var date: String? = null

    private lateinit var recyclerView: RecyclerView

    private lateinit var songAdapter: SongAdapter
    private lateinit var songModel: SongModel
    private var songModelArrayList = ArrayList<SongModel>()

    private lateinit var progressBar: ProgressBar
    private lateinit var progressDialog: ProgressDialog

    private lateinit var params: RequestParams

    internal lateinit var client: OkHttpClient
    internal lateinit var request: Request

    private lateinit var profile_image: CircleImageView
    private lateinit var username: TextView
    private lateinit var toolbar: Toolbar
    private var user: User? = null


    private var firebaseUser: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

    }


    override fun onStart() {
        super.onStart()

        checkUser()
        initializeUI()
        loadiTunesItems()
    }
    //Initializing User Interface
    private fun initializeUI() {
        recyclerView = findViewById(R.id.coin_recycler_view)
        profile_image = findViewById(R.id.profile_image)
        username = findViewById(R.id.username)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""


        auth = FirebaseAuth.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser
        Debugger.logD("firebaseUser " + firebaseUser)


        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser?.uid.toString())

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user = dataSnapshot.getValue(User::class.java)
                Debugger.logD("user " + user)
                username.text = user?.username.toString()
                if (user?.imageURL.equals("default")) {
                    profile_image.setImageResource(R.drawable.note)
                } else {

                    //change this
                    Glide.with(applicationContext).load(user?.imageURL).into(profile_image)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }


    //Load All Items From API
    private fun loadiTunesItems() {
        songModelArrayList.clear()

        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        client = OkHttpClient()
        request = Request.Builder()
            .url(String.format("https://itunes.apple.com/search?term=star&amp;country=au&amp;media=movie&amp;all"))
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    Log.d("ERROR", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    progressDialog.dismiss()

                    var responses = JSONObject(response.body!!.string())
                    Debugger.logD("ohhhh" + responses)

                    for (i in 0 until responses.getJSONArray("results").length()) {
                        val jsonObject = responses.getJSONArray("results").getJSONObject(i)

                        val wrapperType = jsonObject.optString("wrapperType")//1
                        val kind = jsonObject.optString("kind")//2
                        val artistId = jsonObject.optString("artistId")//3
                        val collectionId = jsonObject.optString("collectionId")//4
                        val trackId = jsonObject.optString("trackId")//5
                        val artistName = jsonObject.optString("artistName")//6
                        val collectionName = jsonObject.optString("collectionName")//7
                        val trackName = jsonObject.optString("trackName")//8
                        val collectionCensoredName = jsonObject.optString("collectionCensoredName")//9
                        val trackCensoredName = jsonObject.optString("trackCensoredName")//10
                        val artistViewUrl = jsonObject.optString("artistViewUrl")//11
                        val collectionViewUrl = jsonObject.optString("collectionViewUrl")//12
                        val trackViewUrl = jsonObject.optString("trackViewUrl")//13
                        val previewUrl = jsonObject.optString("previewUrl")//14
                        val artworkUrl30 = jsonObject.optString("artworkUrl30")//15
                        val artworkUrl60 = jsonObject.optString("artworkUrl60")//16
                        val artworkUrl100 = jsonObject.optString("artworkUrl100")//17
                        val collectionPrice = jsonObject.optString("collectionPrice")//18
                        val trackPrice = jsonObject.optString("trackPrice")//19
                        val releaseDate = jsonObject.optString("releaseDate")//20
                        val collectionExplicitness = jsonObject.optString("collectionExplicitness")//21
                        val trackExplicitness = jsonObject.optString("trackExplicitness")//22
                        val discCount = jsonObject.optString("discCount")//23
                        val discNumber = jsonObject.optString("discNumber")//24
                        val trackCount = jsonObject.optString("trackCount")//25
                        val trackNumber = jsonObject.optString("trackNumber")//26
                        val trackTimeMillis = jsonObject.optString("trackTimeMillis")//27
                        val country = jsonObject.optString("country")//28
                        val currency = jsonObject.optString("currency")//29
                        val primaryGenreName = jsonObject.optString("primaryGenreName")//30
                        val isStreamable = jsonObject.optString("isStreamable")//31

                        songModel = SongModel()
                        songModel.wrapperType = wrapperType
                        songModel.kind = kind
                        songModel.artistId = artistId
                        songModel.collectionId = collectionId
                        songModel.trackId = trackId
                        songModel.artistName = artistName
                        songModel.collectionName = collectionName
                        songModel.trackName = trackName
                        songModel.collectionCensoredName = collectionCensoredName
                        songModel.trackCensoredName = trackCensoredName
                        songModel.artistViewUrl = artistViewUrl
                        songModel.collectionViewUrl = collectionViewUrl
                        songModel.trackViewUrl = trackViewUrl
                        songModel.previewUrl = previewUrl
                        songModel.artworkUrl30 = artworkUrl30
                        songModel.artworkUrl60 = artworkUrl60
                        songModel.artworkUrl100 = artworkUrl100
                        songModel.collectionPrice = collectionPrice
                        songModel.trackPrice = trackPrice
                        songModel.releaseDate = releaseDate
                        songModel.collectionExplicitness = collectionExplicitness
                        songModel.trackExplicitness = trackExplicitness
                        songModel.discCount = discCount
                        songModel.discNumber = discNumber
                        songModel.trackCount = trackCount
                        songModel.trackNumber = trackNumber
                        songModel.trackTimeMillis = trackTimeMillis
                        songModel.country = country
                        songModel.currency = currency
                        songModel.primaryGenreName = primaryGenreName
                        songModel.isStreamable = isStreamable

                        songModelArrayList.add(songModel)
                    }




                    runOnUiThread {
                        val layoutManager = LinearLayoutManager(context)
                        recyclerView.layoutManager = layoutManager
                        songAdapter = SongAdapter(this@MainActivity, songModelArrayList)
                        recyclerView.adapter = songAdapter
                        songAdapter.setOnItemClickListener(this@MainActivity)
                    }
                }

            })


    }

    //Checking If There Is A Current User
    private fun checkUser() {
        //firebaseUser = FirebaseAuth.getInstance().currentUser!!
        //check if user is null
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
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
                startActivity(
                    Intent(
                        this@MainActivity,
                        LoginActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                return true
            }
        }

        return false
    }

    //Item Click For Each Item That The User Click
    //
    override fun onItemClick(position: Int) {
        val detailIntent = Intent(context, DetailsActivity::class.java)
        val songModels = songModelArrayList[position]

        detailIntent.putExtra("artworkUrl30", songModels.artworkUrl100)
        detailIntent.putExtra("trackName", songModels.trackName)
        detailIntent.putExtra("trackPrice", songModels.trackPrice)
        detailIntent.putExtra("primaryGenreName", songModels.primaryGenreName)

        startActivity(detailIntent)
    }



}



