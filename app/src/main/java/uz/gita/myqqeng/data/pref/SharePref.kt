package uz.gita.myqqeng.data.pref

import android.content.Context
import uz.gita.myqqeng.app.App

class SharePref private constructor(){
    companion object {
        private lateinit var instance: SharePref


        fun getSharePref() :SharePref{
            if (!::instance.isInitialized) {
                instance = SharePref()
            }
            return instance
        }
    }

    private val pref = App.instance.getSharedPreferences("Dictionary", Context.MODE_PRIVATE)

    var isFirstRunning
    get() = pref.getBoolean("firstRunning", true)
    set(value) = pref.edit().putBoolean("firstRunning", value).apply()
}