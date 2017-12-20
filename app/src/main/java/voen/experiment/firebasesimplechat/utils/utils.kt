package voen.experiment.firebasesimplechat.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import java.util.*

/**
 * Created by voen on 12/6/17.
 */

fun String.log() {
    Log.d("voen", this)
}

fun View.showVisibility() {
    this.visibility = View.VISIBLE
}

fun View.hideVisibility() {
    this.visibility = View.INVISIBLE
}

fun View.disable() {
    this.isEnabled = false
}

fun View.enable() {
    this.isEnabled = true
}

fun Activity.checkGoogleAPI() {
    val googleApi = GoogleApiAvailability.getInstance()
    val resultCode = googleApi.isGooglePlayServicesAvailable(this.baseContext)
    if (resultCode != ConnectionResult.SUCCESS) {
        googleApi.makeGooglePlayServicesAvailable(this)
    }
}

fun ClosedRange<Int>.random() = Random().nextInt(endInclusive + 1 - start) + start

fun String.cutBy(size: Int) = "${substring(0, if (size > this.length) this.length else size)}..."