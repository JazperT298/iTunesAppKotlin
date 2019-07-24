package com.example.itunesappkotlin.adapters

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.itunesappkotlin.MainActivity
import com.example.itunesappkotlin.R
import com.example.itunesappkotlin.models.SongModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_layout.view.*
import java.util.ArrayList


class SongAdapter(internal var activity: MainActivity, var mList:ArrayList<SongModel>) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(activity)
        val view = layoutInflater.inflate(R.layout.list_layout, viewGroup, false)

        return SongViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: SongViewHolder, i: Int) {

        val songModel = mList[i]


        var image = viewHolder.songIcon
        var trackName: TextView = viewHolder.trackName
        var price: TextView  = viewHolder.price
        var genre: TextView = viewHolder.genre

        trackName.text = songModel.trackName
        price.text = songModel.trackPrice
        genre.text = songModel.primaryGenreName


        if (songModel.artworkUrl100!!.isEmpty()) {
            image.setImageResource(R.drawable.note)
        } else {
            Picasso.get()
                .load(songModel.artworkUrl100)
                .into(image)
        }
        if (songModel.trackPrice.equals("")) {
            price.text = "0.00"
        } else {
            price.text = songModel.trackPrice
        }

        if (songModel.primaryGenreName.equals("Action & Adventure")) {
            genre.setTextColor(Color.parseColor("#FF0000"))
        } else if (songModel.primaryGenreName.equals("Pop")) {
            genre.setTextColor(Color.parseColor("#32CD32"))
        } else if (songModel.primaryGenreName.equals("Sci-Fi & Fantasy")) {
            genre.setTextColor(Color.parseColor("#3399FF"))
        } else if (songModel.primaryGenreName.equals("K-Pop")) {
            genre.setTextColor(Color.parseColor("#FF66FF"))
        } else {
            genre.setTextColor(Color.parseColor("#00574B"))
        }



    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var trackName = itemView.trackName
        var price = itemView.price
        var genre = itemView.genre
        var songIcon = itemView.songIcon

        init {

            songIcon = itemView.findViewById(R.id.songIcon)
            trackName = itemView.findViewById(R.id.trackName)
            price = itemView.findViewById(R.id.price)
            genre = itemView.findViewById(R.id.genre)

            itemView.setOnClickListener {
                if (onItemClickListener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener!!.onItemClick(position)
                    }
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(onItemClickListeners: OnItemClickListener) {
        onItemClickListener = onItemClickListeners
    }


}
