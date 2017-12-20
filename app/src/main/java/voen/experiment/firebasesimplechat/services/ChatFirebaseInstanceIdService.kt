package voen.experiment.firebasesimplechat.services

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by voen on 12/8/17.
 */
class ChatFirebaseInstanceIdService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d("voen","token 2 : $refreshedToken")
    }
}