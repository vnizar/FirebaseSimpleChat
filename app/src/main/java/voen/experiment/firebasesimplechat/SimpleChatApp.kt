package voen.experiment.firebasesimplechat

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import voen.experiment.firebasesimplechat.helper.SharedPreferenceHelper

/**
 * Created by voen on 12/6/17.
 */
class SimpleChatApp : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        SharedPreferenceHelper.initialize(this)
    }
}