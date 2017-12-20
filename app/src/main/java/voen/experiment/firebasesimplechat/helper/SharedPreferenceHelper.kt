package voen.experiment.firebasesimplechat.helper

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by voen on 12/7/17.
 */
object SharedPreferenceHelper {
    val SHARED_PREFERENCE_NAME = "simple_chat_pref"
    val FIREBASE_TOKEN = "firebase_token"
    val FIREBASE_USER_NAME = "firebase_username"
    val FIREBASE_UID = "firebase_uid"

    private lateinit var sharedPreference: SharedPreferences

    fun initialize(context: Context){
        sharedPreference = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0)
    }

    fun setBoolean(key: String, active: Boolean) {
        sharedPreference.edit().putBoolean(key, active).apply()
    }

    fun setString(key: String, value: String?) {
        sharedPreference.edit().putString(key, value).apply()
    }

    fun setInt(key: String, value: Int) {
        sharedPreference.edit().putInt(key, value).apply()
    }

    fun setLong(key: String, value: Long) {
        sharedPreference.edit().putLong(key, value).apply()
    }

    fun getBoolean(key: String) = sharedPreference.getBoolean(key, false)

    fun getString(key: String) = sharedPreference.getString(key, "")

    fun getInt(key: String) = sharedPreference.getInt(key, 0)

    fun getLong(key: String) = sharedPreference.getLong(key, 0)

    fun clearAll(){
        sharedPreference.edit().clear().apply()
    }
}