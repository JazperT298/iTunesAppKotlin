package com.example.itunesappkotlin.models

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class SongModel {
    var wrapperType: String? = null//1
    var kind: String? = null//2
    var artistId: String? = null//3
    var collectionId: String? = null//4
    var trackId: String? = null//5
    var artistName: String? = null//6
    var collectionName: String? = null//7
    var trackName: String? = null//8
    var collectionCensoredName: String? = null//9
    var trackCensoredName: String? = null//10
    var artistViewUrl: String? = null//11
    var collectionViewUrl: String? = null//12
    var trackViewUrl: String? = null//13
    var previewUrl: String? = null//14
    var artworkUrl30: String? = null//15
    var artworkUrl60: String? = null//16
    var artworkUrl100: String? = null//17
    var collectionPrice: String? = null//18
    var trackPrice: String? = null//19
    var releaseDate: String? = null//20
    var collectionExplicitness: String? = null//21
    var trackExplicitness: String? = null//22
    var discCount: String? = null//23
    var discNumber: String? = null//24
    var trackCount: String? = null//25
    var trackNumber: String? = null//26
    var trackTimeMillis: String? = null//27
    var country: String? = null//28
    var currency: String? = null//29
    var primaryGenreName: String? = null//30
    var isStreamable: String? = null//31


    constructor(
        wrapperType: String,
        kind: String,
        artistId: String,
        collectionId: String,
        trackId: String,
        artistName: String,
        collectionName: String,
        trackName: String,
        collectionCensoredName: String,
        trackCensoredName: String,
        artistViewUrl: String,
        collectionViewUrl: String,
        trackViewUrl: String,
        previewUrl: String,
        artworkUrl30: String,
        artworkUrl60: String,
        artworkUrl100: String,
        collectionPrice: String,
        trackPrice: String,
        releaseDate: String,
        collectionExplicitness: String,
        trackExplicitness: String,
        discCount: String,
        discNumber: String,
        trackCount: String,
        trackNumber: String,
        trackTimeMillis: String,
        country: String,
        currency: String,
        primaryGenreName: String,
        isStreamable: String
    ) {
        this.wrapperType = wrapperType
        this.kind = kind
        this.artistId = artistId
        this.collectionId = collectionId
        this.trackId = trackId
        this.artistName = artistName
        this.collectionName = collectionName
        this.trackName = trackName
        this.collectionCensoredName = collectionCensoredName
        this.trackCensoredName = trackCensoredName
        this.artistViewUrl = artistViewUrl
        this.collectionViewUrl = collectionViewUrl
        this.trackViewUrl = trackViewUrl
        this.previewUrl = previewUrl
        this.artworkUrl30 = artworkUrl30
        this.artworkUrl60 = artworkUrl60
        this.artworkUrl100 = artworkUrl100
        this.collectionPrice = collectionPrice
        this.trackPrice = trackPrice
        this.releaseDate = releaseDate
        this.collectionExplicitness = collectionExplicitness
        this.trackExplicitness = trackExplicitness
        this.discCount = discCount
        this.discNumber = discNumber
        this.trackCount = trackCount
        this.trackNumber = trackNumber
        this.trackTimeMillis = trackTimeMillis
        this.country = country
        this.currency = currency
        this.primaryGenreName = primaryGenreName
        this.isStreamable = isStreamable
    }

    constructor() {

    }

    companion object {

        // Returns a Book given the expected JSON
        fun fromJson(jsonObject: JSONObject): SongModel? {
            val songModel = SongModel()
            try {
                // Deserialize json into object fields
                // Check if a cover edition is available
                if (jsonObject.has("trackName")) {
                    songModel.trackName = jsonObject.getString("trackName")
                }
                //            else if(jsonObject.has("edition_key")) {
                //                final JSONArray ids = jsonObject.getJSONArray("edition_key");
                //                songModel.trackName = ids.getString(0);
                //            }
                songModel.artworkUrl100 =
                    if (jsonObject.has("artworkUrl00")) jsonObject.getString("artworkUrl00") else ""
                songModel.trackPrice = if (jsonObject.has("trackPrice")) jsonObject.getString("trackPrice") else ""
                songModel.primaryGenreName =
                    if (jsonObject.has("primaryGenreName")) jsonObject.getString("primaryGenreName") else ""
            } catch (e: JSONException) {
                e.printStackTrace()
                return null
            }

            // Return new object
            return songModel
        }

        // Decodes array of book json results into business model objects
        fun fromJson(jsonArray: JSONArray): ArrayList<SongModel> {
            val songModels = ArrayList<SongModel>(jsonArray.length())
            // Process each result in json array, decode and convert to business
            // object
            for (i in 0 until jsonArray.length()) {
                var bookJson: JSONObject? = null
                try {
                    bookJson = jsonArray.getJSONObject(i)
                } catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }

                val songModel = SongModel.fromJson(bookJson!!)
                if (songModel != null) {
                    songModels.add(songModel)
                }
            }
            return songModels
        }
    }
}
