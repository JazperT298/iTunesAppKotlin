package com.example.ituneskotlin.utils

import android.content.Context
import android.preference.PreferenceManager

internal class SharedPref {

    var name: String? = null

    fun saveUserSession(context: Context): Boolean {
        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = settings.edit()
        editor.putString("NAME", name)

        return editor.commit()
    }

    companion object {
        val FileName = "MyFileName"

        fun readSharedSetting(ctx: Context, settingName: String, defaultValue: String): String? {
            val sharedPref = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE)
            return sharedPref.getString(settingName, defaultValue)
        }


        fun saveSharedSetting(ctx: Context, settingName: String, settingValue: String) {
            val sharedPref = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(settingName, settingValue)
            editor.apply()
        }

        fun SharedPrefesSAVE(ctx: Context, Name: String, date: String) {
            val prefs = ctx.getSharedPreferences("NAME", 0)
            val prefEDIT = prefs.edit()
            prefEDIT.putString("Name", Name)
            prefEDIT.putString("Date", date)
            prefEDIT.commit()
        }
    }

}
