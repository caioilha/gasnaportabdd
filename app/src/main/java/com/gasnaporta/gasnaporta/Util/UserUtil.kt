package com.gasnaporta.gasnaporta.Util

import android.content.Context

import android.content.SharedPreferences
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.util.Log
import com.gasnaporta.gasnaporta.Model.OutLogin

private const val TOKEN = "token"
private const val USER_ID = "userId"
private const val USER_NAME = "userName"
private const val USER_EMAIL = "userEmail"

class UserUtil {


    fun getToken(context: Context): String {
        Log.e("token", getSharedPreferences(context).getString(TOKEN, ""))
        return getSharedPreferences(context).getString(TOKEN, "")
    }

    fun getUserId(context: Context): String {
        return getSharedPreferences(context).getString(USER_ID, "")
    }

    fun getUserName(context: Context): String {
        return getSharedPreferences(context).getString(USER_NAME, "")
    }

    fun getUserEmail(context: Context): String {
        return getSharedPreferences(context).getString(USER_EMAIL, "")
    }

    fun login(context: Context, outLogin: OutLogin) {
        val editor = getSharedPreferences(context).edit()
        outLogin.user.let {
            editor.putString(USER_ID, "${outLogin.user._id}")
            editor.putString(USER_NAME, "${outLogin.user.name}")
            editor.putString(USER_EMAIL, "${outLogin.user.email}")
        }
        Log.e("login user util", outLogin.token)
        editor.putString(TOKEN, "bearer ${outLogin.token}")
        editor.commit()
    }

    fun logout(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.clear().commit()
        editor.putString(USER_ID, "")
        editor.putString(TOKEN, "")
        editor.commit()
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return getDefaultSharedPreferences(context.applicationContext)

    }
}
