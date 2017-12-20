package voen.experiment.firebasesimplechat.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import voen.experiment.firebasesimplechat.helper.getFirebaseAuth
import voen.experiment.firebasesimplechat.helper.getFirebaseDatabase
import voen.experiment.firebasesimplechat.utils.checkGoogleAPI

/**
 * Created by voen on 12/13/17.
 */
abstract class BaseActivity : AppCompatActivity() {
    protected val firebaseAuth by lazy {
        this.getFirebaseAuth()
    }
    protected val firebaseDatabase by lazy {
        this.getFirebaseDatabase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLayout()
        onActivityCreated()
    }

    override fun onResume() {
        super.onResume()
        this.checkGoogleAPI()
    }

    abstract fun setupLayout()
    abstract fun onActivityCreated()
}