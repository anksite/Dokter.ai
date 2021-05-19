package com.dokter.ai.util

import android.content.Context

class SpHelp (context: Context){
    val mContext = context
    val sp = mContext.getSharedPreferences("data", 0)

    fun writeInt(key:String, value:Int){
        sp.edit().putInt(key, value).apply()
    }

    fun getInt(key:String):Int{
        return sp.getInt(key, 0)
    }

    fun writeString(key:String, value:String){
        sp.edit().putString(key, value).apply()
    }

    fun getString(key:String):String{
        return sp.getString(key, "NULL")?:"NULL"
    }

    fun writeBool(key:String, value:Boolean){
        sp.edit().putBoolean(key, value).apply()
    }

    fun getBool(key:String):Boolean{
        return sp.getBoolean(key, false)
    }
}