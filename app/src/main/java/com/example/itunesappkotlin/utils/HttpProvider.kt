package com.example.ituneskotlin.utils

import android.content.Context
import com.loopj.android.http.*
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

internal class HttpProvider {

    private fun getApiUrl(relativeUrl: String): String {
        return BASE_URL + relativeUrl
    }

    // Method for accessing the search API
    fun getiTunes(query: String, handler: JsonHttpResponseHandler) {
        try {
            val url = getApiUrl("&amp;artistName=")
            client.get(url + URLEncoder.encode(query, "utf-8"), handler)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

    }

    // Method for accessing books API to get publisher and no. of pages in a book.
    fun getExtraBookDetails(openLibraryId: String, handler: JsonHttpResponseHandler) {
        val url = getApiUrl("")
        client.get("$url$openLibraryId.json", handler)
    }

    companion object {

        private val BASE_URL = "https://itunes.apple.com/"


        private val client = AsyncHttpClient()
        private val syncHttpClient = SyncHttpClient()


        fun defaultPost(ctx: Context, url: String, params: RequestParams, responseHandler: AsyncHttpResponseHandler) {
            /* Fixed Login problem where second login attempt after logout changes content type of data */
            /* Problem - Can't Login after logout from previous account */
            /* Cause - Content type is changed or has been added other type due to other http call (read_inventory) */
            /* Solution - Create new AssyncHttp Instance to ensure no previous content type or header has been set */

            val asyncHttpClient = AsyncHttpClient()
            asyncHttpClient.post(ctx, getAbsoluteUrl(url), params, responseHandler)
        }


        private fun getAbsoluteUrl(relativeUrl: String): String {
            return BASE_URL + relativeUrl
        }

        val baseURL: String
            get() = BASE_URL.replace("api/", "")
    }
}
