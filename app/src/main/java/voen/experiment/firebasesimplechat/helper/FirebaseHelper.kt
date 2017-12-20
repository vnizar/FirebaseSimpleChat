package voen.experiment.firebasesimplechat.helper

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId

/**
 * Created by voen on 12/7/17.
 */
fun Context.getFirebaseAuth() : FirebaseAuth = FirebaseAuth.getInstance()
fun Context.getFirebaseDatabase() : FirebaseDatabase = FirebaseDatabase.getInstance()
fun Context.getFirebaseInstanceId() : FirebaseInstanceId = FirebaseInstanceId.getInstance()