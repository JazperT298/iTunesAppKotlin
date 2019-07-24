package com.example.ituneskotlin.utils

import android.util.Log
import com.google.gson.Gson

internal object Debugger {


    val TAG = "YES"


    fun printO(obj: Any): String {
        val gson = Gson()
        println(gson.toJson(obj))
        Log.d(TAG, gson.toJson(obj))
        return gson.toJson(obj)
    }

    fun logD(message: String) {
        Log.d(TAG, message)
    }

    fun printError(err: Exception) {
        Log.d(TAG, "Line No :" + err.stackTrace[0].lineNumber)
        Log.d(TAG, err.message)
        Log.d(TAG, err.toString())
        err.printStackTrace()
    }

    fun logDint(value: Int) {
        Log.d(TAG, value.toString())
    }

}
